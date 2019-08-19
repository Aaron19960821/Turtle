package com.turtle.yucwang.turtle

import com.turtle.yucwang.turtle.Data.Converters
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DataConverterTest {

    private val dateString = "2019-01-02"

    @Test fun testDateToString() {
        val calendar = Calendar.getInstance()
        calendar.set(2019, 0, 2)
        val date = calendar.time
        assertEquals(dateString, Converters().dateToString(date))
    }

    @Test fun testStringToDate() {
        val calendar = Calendar.getInstance()
        calendar.set(2019, 0, 2, 0, 0, 0)
        val date = calendar.time
        val dateConverted = Converters().stringToDate(dateString)
        assertEquals(date.year, dateConverted.year)
        assertEquals(date.month, dateConverted.month)
        assertEquals(date.day, dateConverted.day)
    }
}