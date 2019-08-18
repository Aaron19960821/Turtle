package com.turtle.yucwang.turtle.Data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @TypeConverter fun dateToString(date: Date) = dateFormat.format(date)

    @TypeConverter fun stringToDate(dateString: String) = dateFormat.parse(dateString)
}
