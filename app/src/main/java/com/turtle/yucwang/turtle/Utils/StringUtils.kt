package com.turtle.yucwang.turtle.Utils

import android.content.Context
import com.turtle.yucwang.turtle.R
import java.util.concurrent.TimeUnit

class StringUtils() {
    companion object {
        fun convertMillsecondsToString(context: Context, time: Long): String {
            var result = String()
            val mins = TimeUnit.MILLISECONDS.toMinutes(time).toInt()
            if (mins >= 60) {
                result += (mins / 60).toString()
                result += context.getString(R.string.hour)
            }

            if (mins % 60 != 0 || mins == 0) {
                result += (mins % 60).toString()
                result += context.getString(R.string.minute)
            }

            return result
        }
    }
}