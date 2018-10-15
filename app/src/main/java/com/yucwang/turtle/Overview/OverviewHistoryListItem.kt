// @Copyright Yuchen Wang 2018

package com.yucwang.turtle.Overview

import java.text.SimpleDateFormat
import java.util.*

/**
 * History list item data description
 * @mDate: the date of record
 * @mTimeUsage: the length of time we use on that day
 */
class OverviewHistoryListItem(val mDate: Date, val mTimeUsage: Long) {

    companion object {
        private val DATE_ENCODING_FORMAT = "yyyy-MM-dd"
        private val MINUTE_STRING = "min"
        private val HOUR_STRING = "h"
        private val TODAY_STRING = "Today"
        private val YESTERDAY_STRING = "Yesterday"
        private val MINUTE_PER_HOUR = 60
        private val MILLISECONDS_PER_MINUTE = 60000

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

        /**
         * Return an Instance with data from database
         */
        fun getInstanceFromDatabase(dateString: String, usage: Long): OverviewHistoryListItem {
            val date: Date = SimpleDateFormat(DATE_ENCODING_FORMAT).parse(dateString)
            return OverviewHistoryListItem(date, usage)
        }

        fun isAlertTimeUsage(time: Long): Boolean {
            val minute = time / MILLISECONDS_PER_MINUTE
            return minute > 120
        }
    }

    fun Date.isCurrentDay(): Boolean {
        if (getDayInt() == Calendar.getInstance().get(Calendar.DATE) &&
                getMonthInt() == Calendar.getInstance().get(Calendar.MONTH) &&
                getYearInt() == Calendar.getInstance().get(Calendar.YEAR)) return true
        return false
    }

    fun Date.isYesterday(): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        if (getDayInt() == calendar.get(Calendar.DATE) && getMonthInt() == calendar.get(Calendar.MONTH) &&
                getYearInt() == calendar.get(Calendar.YEAR)) return true
        return false
    }

    fun Date.isCurrentYear(): Boolean {
        if (getYearInt() == Calendar.getInstance().get(Calendar.YEAR)) return true
        return false
    }

    /**
     * Return the day, month, year in integer since the functions
     * in Java has been deprecated.
     */
    fun Date.getDayInt(): Int {
        return SimpleDateFormat(DATE_ENCODING_FORMAT).format(this).split("-".toRegex())[2].toInt()
    }

    fun Date.getMonthInt(): Int {
        return SimpleDateFormat(DATE_ENCODING_FORMAT).format(this).split("-".toRegex())[1].toInt()
    }

    fun Date.getYearInt(): Int {
        return SimpleDateFormat(DATE_ENCODING_FORMAT).format(this).split("-".toRegex())[0].toInt()
    }

    // get the suffix of a date
    fun Date.getSuffix(): String {
        when (getDayInt() % 10) {
            1 -> return "st"
            2 -> return "nd"
            3 -> return "rd"
            else -> return "th"
        }
    }

    fun encodeDateForDataBase(): String {
        return SimpleDateFormat(DATE_ENCODING_FORMAT).format(mDate)
    }

    fun encodeUsageForDatabase(): Long {
        return mTimeUsage
    }

    fun getFormattedDate(): String {
        if (mDate.isCurrentDay()) {
            return TODAY_STRING
        } else if (mDate.isYesterday()) {
            return YESTERDAY_STRING
        } else if (mDate.isCurrentYear()) {
            return String.format("%s %d%s", MONTHS_LIST[mDate.getMonthInt() - 1], mDate.getDayInt(), mDate.getSuffix())
        } else {
            return String.format("%s %d%s %d", MONTHS_LIST[mDate.getMonthInt() - 1], mDate.getDayInt(), mDate.getSuffix(),
                    mDate.getYearInt())
        }
    }

    fun getFormattedUsage(): String {
        val totalMins = mTimeUsage / MILLISECONDS_PER_MINUTE
        val hour = totalMins / MINUTE_PER_HOUR
        val minute = totalMins % MINUTE_PER_HOUR
        var formattedUsage: String

        if (hour > 0) {
            formattedUsage = String.format("%d%s%d%s", hour, HOUR_STRING, minute, MINUTE_STRING)
        } else {
            formattedUsage = String.format("%d%s", minute, MINUTE_STRING)
        }

        return formattedUsage
    }
}