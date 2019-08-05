package com.turtle.yucwang.turtle.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface DailyUsageDao {
    @Query("SELECT * FROM daily_usage ORDER BY date")
    fun getAllDailyUsage(): LiveData<LiveData<DailyUsage>>

    @Query("SELECT * FROM daily_usage WHERE date = :date")
    fun getDailyUsageByDate(date: Date): LiveData<DailyUsage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dailyUsage: DailyUsage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dailyUsages: List<DailyUsage>)
}