package com.turtle.yucwang.turtle.AppUsage

import java.util.concurrent.TimeUnit

class AppUsageUtils() {
    companion object {
        fun isAppUsageAlert(timeInMills: Long): Boolean {
            val mins = TimeUnit.MILLISECONDS.toMinutes(timeInMills).toInt()
            return mins > 4 * 60
        }
    }
}