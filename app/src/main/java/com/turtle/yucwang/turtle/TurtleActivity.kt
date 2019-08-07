package com.turtle.yucwang.turtle

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DailyUsageListApapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var viewModel: DailyUsageViewModel

    private lateinit var timeDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dailyusage_list)

        supportActionBar!!.hide()

        timeDisplay = findViewById(R.id.today_daily_usage)

        viewManager = LinearLayoutManager(this@TurtleActivity)
        viewAdapter = DailyUsageListApapter(this@TurtleActivity)
        recyclerView = findViewById(R.id.daily_usages_list)
        recyclerView.apply {
            adapter = viewAdapter
            layoutManager = viewManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@TurtleActivity, LinearLayoutManager.VERTICAL))
        }

        val appUsageManager = AppUsageManager(this)
        appUsageManager.updateAppUsage()

        viewModel = ViewModelProviders.of(this).get(DailyUsageViewModel::class.java)
        viewModel.init(this as Context)
        viewModel.getAllDailyUsages().observe(this, Observer {
            viewAdapter.setData(it)
        })
        viewModel.getTodayDailyUsage().observe(this, Observer {
            timeDisplay.apply {
                text = StringUtils.convertMillsecondsToString(it.usage)
                setTextColor(if (AppUsageUtils.isAppUsageAlert(it.usage))
                    getColor(R.color.colorAlert)
                else getColor(R.color.colorGood))
            }
        })
    }
}
