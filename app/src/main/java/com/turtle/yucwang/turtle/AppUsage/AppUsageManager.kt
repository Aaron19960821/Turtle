package com.turtle.yucwang.turtle.AppUsage

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.AsyncTask
import android.os.Process
import android.util.ArrayMap
import com.turtle.yucwang.turtle.Data.AppDatabase
import com.turtle.yucwang.turtle.Data.Converters
import com.turtle.yucwang.turtle.Data.DailyUsage
import com.turtle.yucwang.turtle.Data.DailyUsageRepository
import com.turtle.yucwang.turtle.Utils.DateUtils
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
            var startOfTodayInMills: Long = DateUtils.getStartOfDayFromTimeStamp(currentTimeInMills)
            val usageStates = mUsageStateManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startOfTodayInMills, currentTimeInMills)
            val aggregatedUsageStates = aggregatedQueryResults(startOfTodayInMills, usageStates)
            UpdateAppUsageTaskWithDbIO(aggregatedUsageStates, listener).execute(context)
        }
    }

    fun checkPermission(): Boolean {
        val appOps: AppOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)

        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun aggregatedQueryResults(startTimestamp: Long, usageStats: List<UsageStats>): Map<String, UsageStats> {
        if (usageStats.isEmpty()) {
            return Collections.emptyMap()
        }

        val aggregatedResults = ArrayMap<String, UsageStats>()
        val stateCount = usageStats.size
        for (i in 0..(stateCount - 1)) {
            val newState = usageStats.get(i)
            if (startTimestamp - newState.firstTimeStamp <= TimeUnit.MINUTES.toMillis(VALID_USAGE_THRESHHOLD_IN_MINS)) {
                val existedUsgaeState = aggregatedResults.get(newState.packageName)
                if (existedUsgaeState != null) {
                    existedUsgaeState.add(newState)
                } else {
                    aggregatedResults.put(newState.packageName, newState)
                }
            }
        }

        return aggregatedResults
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
        // We do not aggregate the results that was collected long ago
        const val VALID_USAGE_THRESHHOLD_IN_MINS = 20L

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