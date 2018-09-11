package com.yucwang.turtle.Backend

import android.annotation.TargetApi
import android.app.Activity
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import com.yucwang.turtle.MainActivity
import com.yucwang.turtle.Overview.OverviewFragment
import java.util.*

class AppUsageManager(mContext: Context): MainActivity.OverViewDataAdapter {

    val MILLSEC_PER_MINUTE: Long = 1000 * 60
    val mUsageStatsManager = mContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    /**
     * Get the Usage of Current Day.
     */
    override fun getCurrentDayAppUsage(): Int {
        val start = getStartOfCurrentDay().timeInMillis
        val end = System.currentTimeMillis()
        val usageList: List<UsageStats> = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                start, end)

        var totalTime = 0L
        for (usageState in usageList) {
            totalTime += usageState.totalTimeInForeground
        }

        return (totalTime / MILLSEC_PER_MINUTE).toInt()
    }

    private fun getStartOfCurrentDay(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, -Calendar.HOUR_OF_DAY)
        calendar.add(Calendar.MINUTE, -Calendar.MINUTE)
        calendar.add(Calendar.SECOND, -Calendar.SECOND)

        return calendar
    }
}