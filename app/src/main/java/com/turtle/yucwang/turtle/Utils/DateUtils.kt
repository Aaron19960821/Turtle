package com.turtle.yucwang.turtle.Utils

import java.util.*

class DateUtils private constructor() {
    companion object {
        fun getStartTimestampOfToday(): Long {
            return getStartOfDayFromTimeStamp(System.currentTimeMillis())
        }

        fun getStartOfDayFromTimeStamp(timeStamp: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            return calendar.timeInMillis
        }

        fun getLastMinuteOfCurrentHour(): Long {
            return getLastMinuteOfHourFromTimeStamp(System.currentTimeMillis())
        }

        fun getLastMinuteOfHourFromTimeStamp(timeStamp: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 0)
            return calendar.timeInMillis
        }
    }
}