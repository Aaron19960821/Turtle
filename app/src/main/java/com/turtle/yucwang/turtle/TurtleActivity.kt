package com.turtle.yucwang.turtle

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yucwang.turtle.Adapter.DailyUsageListApapter
import com.turtle.yucwang.turtle.AppUsage.AppUsageManager
import com.turtle.yucwang.turtle.AppUsage.AppUsageUtils
import com.turtle.yucwang.turtle.Data.AppDatabase
import com.turtle.yucwang.turtle.Data.DailyUsageRepository
import com.turtle.yucwang.turtle.Utils.StringUtils
import com.turtle.yucwang.turtle.ViewModel.DailyUsageViewModel

class TurtleActivity : AppCompatActivity() {

    private lateinit var mAppUsageManager: AppUsageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turtle)

        mAppUsageManager = AppUsageManager(this)
        mAppUsageManager.updateAppUsage()
    }
}
