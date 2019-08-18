package com.turtle.yucwang.turtle.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.turtle.yucwang.turtle.AppUsage.AppUsageManager
import com.turtle.yucwang.turtle.Data.AppDatabase
import com.turtle.yucwang.turtle.Data.DailyUsage
import com.turtle.yucwang.turtle.Data.DailyUsageRepository
import java.util.*

class DailyUsageViewModel : ViewModel() {
    private lateinit var dailyUsageRepository: DailyUsageRepository
    private lateinit var dailyUsages: LiveData<List<DailyUsage>>
    private lateinit var todayDailyUsage: LiveData<DailyUsage>

    fun init(context: Context) {
        dailyUsageRepository =
                DailyUsageRepository.getInstance(AppDatabase.getInstance(context).DailyUsageDao())
        dailyUsages = dailyUsageRepository.getDailyUsages()
        todayDailyUsage = dailyUsageRepository.getDailyUsage(Calendar.getInstance().time)
    }

    fun getAllDailyUsages(): LiveData<List<DailyUsage>> {
        return dailyUsages
    }

    fun getTodayDailyUsage(): LiveData<DailyUsage> {
        return todayDailyUsage
    }
}