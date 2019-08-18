package com.turtle.yucwang.turtle.AppUsage

import android.app.AlertDialog
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Process
import android.provider.Settings
import android.widget.Toast
import com.turtle.yucwang.turtle.Data.AppDatabase
import com.turtle.yucwang.turtle.Data.Converters
import com.turtle.yucwang.turtle.Data.DailyUsage
import com.turtle.yucwang.turtle.Data.DailyUsageRepository
import com.turtle.yucwang.turtle.R
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class AppUsageManager private constructor(val context: Context) {
    private val mUsageStateManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    interface OnAppUsageUpdatedListener {
        fun onAppUsageUpdated(currentDayData: DailyUsage)
    }

    fun updateAppUsage(listener: OnAppUsageUpdatedListener?) {
        if (!this.checkPermission()) {
            return
        } else {
            val currentTimeInMills = System.currentTimeMillis()

            val calendar = Calendar.getInstance()
            var startOfTodayInMills: Long = currentTimeInMills
            startOfTodayInMills -= TimeUnit.HOURS.toMillis(calendar.get(Calendar.HOUR_OF_DAY).toLong())
            startOfTodayInMills -= TimeUnit.MINUTES.toMillis(calendar.get(Calendar.MINUTE).toLong())

            val aggregatedUsageStates = mUsageStateManager.queryAndAggregateUsageStats(startOfTodayInMills, currentTimeInMills)
            UpdateAppUsageTaskWithDbIO(aggregatedUsageStates, listener).execute(context)
        }
    }

    fun checkPermission(): Boolean {
        val appOps: AppOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)

        return mode == AppOpsManager.MODE_ALLOWED
    }

    private class UpdateAppUsageTaskWithDbIO(
            val aggregatedUsageState: Map<String, UsageStats>,
            val listener: OnAppUsageUpdatedListener?):
            AsyncTask<Context, Void?, DailyUsage>() {
        override fun doInBackground(vararg parms: Context): DailyUsage {
            var totalUsageInMills: Long = 0
            val date = Converters().dateToString(Calendar.getInstance().time)
            val pickUpTime = 0
            val jsonArray = JSONArray()

            for ((packageName, appUsage) in aggregatedUsageState) {
                totalUsageInMills += appUsage.totalTimeInForeground
                val currentJsonObject = JSONObject()
                currentJsonObject.put("package_name", packageName)
                currentJsonObject.put("package_usage", appUsage.totalTimeInForeground)
                jsonArray.put(currentJsonObject)
            }

            val description = jsonArray.toString()
            val currentDailyUsage = DailyUsage(date, pickUpTime, totalUsageInMills, description)
            val dailyUsageRepository =
                    DailyUsageRepository.getInstance(AppDatabase.getInstance(parms.get(0)).DailyUsageDao())
            dailyUsageRepository.insertDailyUsage(currentDailyUsage)

            return currentDailyUsage
        }

        override fun onPostExecute(result: DailyUsage) {
            if (listener != null) {
                listener.onAppUsageUpdated(result)
            }
        }
    }

    companion object {
        @Volatile private var instance: AppUsageManager? = null

        fun getInstance(context: Context): AppUsageManager {
            if (instance == null) {
                synchronized(this) {
                    instance = AppUsageManager(context.applicationContext)
                    return instance!!
                }
            } else {
                return instance!!
            }
        }
    }
}