/* Copyright 2019 Yuchen Wang */

package com.turtle.yucwang.turtle.Data

import java.util.*

class DailyUsageRepository private constructor(private val dailyUsageDao: DailyUsageDao) {
    fun getDailyUsages() = dailyUsageDao.getAllDailyUsage()

    fun getDailyUsage(date: Date) = dailyUsageDao.getDailyUsageByDate(date)

    fun insertDailyUsage(dailyUsage: DailyUsage) = dailyUsageDao.insert(dailyUsage)

    fun insertDailyUsage(dailyUsages: List<DailyUsage>) = dailyUsageDao.insertAll(dailyUsages)

    companion object {
        @Volatile private var instance: DailyUsageRepository? = null

        fun getInstance(dailyUsageDao: DailyUsageDao) =
                instance ?: synchronized(this) {
                    instance ?: DailyUsageRepository(dailyUsageDao).also { instance = it }
                }
    }
}