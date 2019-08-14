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
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class AppUsageManager(val context: Context) {
    private val mUsageStateManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    fun updateAppUsage() {
        if (!this.checkPermission()) {
            showAcquirePermissionDialog()
        } else {
            val currentTimeInMills = System.currentTimeMillis()

            val calendar = Calendar.getInstance()
            var startOfTodayInMills: Long = currentTimeInMills
            startOfTodayInMills -= TimeUnit.HOURS.toMillis(calendar.get(Calendar.HOUR_OF_DAY).toLong())
            startOfTodayInMills -= TimeUnit.MINUTES.toMillis(calendar.get(Calendar.MINUTE).toLong())

            val aggregatedUsageStates = mUsageStateManager.queryAndAggregateUsageStats(startOfTodayInMills, currentTimeInMills)
            UpdateAppUsageTaskWithDbIO(aggregatedUsageStates).execute(context)
        }
    }

    private fun checkPermission(): Boolean {
        val appOps: AppOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)

        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun showAcquirePermissionDialog() {
        val askPermissionDialogBuilder = AlertDialog.Builder(context)
                .setTitle("Ask for permission")
                .setMessage("We need usage state to track your mobile phone usage state for you")
                .setPositiveButton("Yes", {dialog, which -> context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))})
                .setNegativeButton("No", {dialog, which ->  Toast.makeText(context, "not allowed!!", Toast.LENGTH_LONG)})

        askPermissionDialogBuilder.create().show()
    }

    private class UpdateAppUsageTaskWithDbIO(
            val aggregatedUsageState: Map<String, UsageStats>):
            AsyncTask<Context, Void?, Void?>() {
        override fun doInBackground(vararg parms: Context): Void? {
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
            val dailyUsageRepository =
                    DailyUsageRepository.getInstance(AppDatabase.getInstance(parms.get(0)).DailyUsageDao())
            dailyUsageRepository.insertDailyUsage(DailyUsage(date, pickUpTime, totalUsageInMills, description))

            return null
        }

        override fun onPostExecute(result: Void?) {
        }
    }
}