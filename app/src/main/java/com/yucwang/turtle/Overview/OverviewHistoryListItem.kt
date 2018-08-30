// @Copyright Yuchen Wang 2018

package com.yucwang.turtle.Overview

import android.text.format.Time
import java.util.*

/**
 * History list item data description
 * @mDate: the date of record
 * @mTimeUsage: the length of time we use on that day
 */
class OverviewHistoryListItem(val mDate: Date, val mTimeUsage: Time) {

    companion object {
        private val MINUTE_STRING = "min"
        private val HOUR_STRING = "h"
        private val TODAY_STRING = "Today"
        private val YESTERDAY_STRING = "Yesterday"

        private val MONTHS_LIST: Array<String> = arrayOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "June",
                "July",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
        )

        // get the suffix of a date
        private fun getDateSuffix(date: Date): String {
            when (date.day % 10) {
                1 -> return "st"
                2 -> return "nd"
                3 -> return "rd"
                else -> return "th"
            }
        }

        private fun isToday(date: Date): Boolean {
            if (date.day == Calendar.DAY_OF_MONTH && date.month == Calendar.MONTH
                    && date.year == Calendar.YEAR) return true
            return false
        }

        private fun isYesterday(date: Date): Boolean {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -1)
            if (date.day == calendar.time.day && date.month == calendar.time.month &&
                    date.year == calendar.time.year) return true
            return false
        }

        private fun isSameYear(date: Date): Boolean {
            if (date.year == Calendar.YEAR) return true
            return false
        }
    }

    fun getFormattedDate(): String {
        if (isToday()) {
            return TODAY_STRING
        } else if (isYesterday()) {
            return YESTERDAY_STRING
        } else if (isSameYear(mDate)){
            return String.format("%s %d%s", MONTHS_LIST[mDate.month - 1], mDate.day, getDateSuffix(mDate))
        } else {
            return String.format("%s %d%s %d", MONTHS_LIST[mDate.year - 1], mDate.day, getDateSuffix(mDate),
                    mDate.year)
        }
    }

    fun getFormattedUsage(): String {
        val hour = mTimeUsage.hour
        val minute = mTimeUsage.minute
        var formattedUsage: String

        if (hour > 0) {
            formattedUsage = String.format("%d%s%d%s", hour, HOUR_STRING, minute, MINUTE_STRING)
        } else {
            formattedUsage = String.format("%d%s", minute, MINUTE_STRING)
        }

        return formattedUsage
    }
}