package com.turtle.yucwang.turtle.Utils

import java.util.concurrent.TimeUnit

class StringUtils() {
    companion object {
        fun convertMillsecondsToString(time: Long): String {
            var result: String = String()
            val mins = TimeUnit.MILLISECONDS.toMinutes(time).toInt()
            if (mins >= 60) {
                result += (mins / 60).toString()
                result += 'h'
            }

            if (mins % 60 != 0) {
                result += (mins % 60).toString()
                result += "min"
            }

            return result
        }
    }
}