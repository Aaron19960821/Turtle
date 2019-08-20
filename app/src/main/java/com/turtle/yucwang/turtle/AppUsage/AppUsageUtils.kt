package com.turtle.yucwang.turtle.AppUsage

import android.content.Context
import com.turtle.yucwang.turtle.Preferences.PrefsService

class AppUsageUtils() {
    companion object {
        fun isAppUsageAlert(context: Context, timeInMills: Long): Boolean {
            val appUsageLimit = PrefsService.getInstance(context).getAppUsageLimit()
            return timeInMills >= appUsageLimit
        }

        fun isPhoneUsageAlert(context: Context, timeInMills: Long): Boolean {
            val phoneUsageLimit = PrefsService.getInstance(context).getPhoneUsageLimit()
            return timeInMills >= phoneUsageLimit
        }
    }
}