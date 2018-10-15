package com.yucwang.turtle.Backend

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.text.format.Time
import android.util.Log
import com.yucwang.turtle.MainActivity
import java.util.*

class AppUsageManager(mContext: Context): MainActivity.OverViewDataAdapter {
    private val TAG = "AppUsageManager"

    companion object {
        fun getStartOfCurrentDay(): Calendar {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.HOUR, -Calendar.HOUR_OF_DAY)
            calendar.add(Calendar.MINUTE, -Calendar.MINUTE)
            calendar.add(Calendar.SECOND, -Calendar.SECOND)

            return calendar
        }
    }

    val mUsageStatsManager = mContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    /**
     * Get the Usage of Current Day.
     */
    override fun getCurrentDayAppUsage(): Long {
        Log.d(TAG, "Start getting app usage for the current day.")

        val start = getStartOfCurrentDay().timeInMillis
        val end = System.currentTimeMillis()
        val usageList: List<UsageStats> = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                start, end)

        var totalTime = 0L
        for (usageState in usageList) {
            Log.d(TAG, usageState.packageName)

            totalTime += usageState.totalTimeInForeground
        }

        return totalTime
    }

}