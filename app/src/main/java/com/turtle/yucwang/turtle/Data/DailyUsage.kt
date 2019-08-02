package com.turtle.yucwang.turtle.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_usage")
data class DailyUsage (
        @PrimaryKey @ColumnInfo(name = "id") val id: String,
        val date: String,
        val pickUp: Int,
        val usage: Long,
        val description: String
) {
    override fun toString() = id
}