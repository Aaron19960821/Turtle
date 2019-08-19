package com.turtle.yucwang.turtle

import com.turtle.yucwang.turtle.Utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DateUtilsTest {
    @Test fun testStartOfCurrentDay() {
        val calendar = Calendar.getInstance()
        calendar.set(2019, 0, 10)
        val timeStamp = DateUtils.getStartOfDayFromTimeStamp(calendar.timeInMillis)

        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = timeStamp

        assertEquals(calendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(calendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(resultCalendar.get(Calendar.HOUR_OF_DAY), 0)
        assertEquals(resultCalendar.get(Calendar.MINUTE), 0)
        assertEquals(resultCalendar.get(Calendar.SECOND), 0)
    }

    @Test fun testLastMinuteOfCurrentHour() {
        val calendar = Calendar.getInstance()
        val timeStamp = DateUtils.getLastMinuteOfCurrentHour()

        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = timeStamp

        assertEquals(calendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(calendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(resultCalendar.get(Calendar.MINUTE), 59)
        assertEquals(resultCalendar.get(Calendar.SECOND), 0)
    }
}