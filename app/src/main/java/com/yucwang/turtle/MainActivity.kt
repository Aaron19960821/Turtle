package com.yucwang.turtle

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import com.yucwang.turtle.Backend.AppUsageManager
import com.yucwang.turtle.Overview.HistoryListDatabase
import com.yucwang.turtle.Overview.OverviewFragment
import com.yucwang.turtle.Overview.OverviewHistoryListItem
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(){

    interface OverViewDataAdapter {
        /**
         * Refresh the app usage data
         */
        fun getCurrentDayAppUsage(): Int
    }

    private lateinit var mOverViewDataAdapter: OverViewDataAdapter

    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app should get the access of PACKAGE_USAGE_STATS, ask
        // the user when the permission is not granted.
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))

        setContentView(R.layout.activity_main)
        mOverViewDataAdapter = AppUsageManager(this as Context)

        refreshAppUsage()

        initBottomNavigationView()
        initFragment()
    }

    private fun refreshAppUsage() {
        val usageInInt = mOverViewDataAdapter.getCurrentDayAppUsage()
        val calendar = Calendar.getInstance()

        val dateString = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        val historyListItem = OverviewHistoryListItem.getInstanceFromDatabase(dateString, usageInInt)

        // Update today's APP usage
        val database = HistoryListDatabase(this as Context)
        database.insertHistoryList(historyListItem)
        database.close()
    }

    // By default will open Overview Fragment
    private fun initFragment() {
        mFragment = OverviewFragment.newInstance()
        openFragment()
    }

    // init the bottom navigation view
    private fun initBottomNavigationView() {
        mBottomNavigationView = this.findViewById(R.id.app_bottombar)
        mBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottombar_overview -> {
                    val overviewFragment = OverviewFragment.newInstance()
                    mFragment = overviewFragment
                    openFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_record -> {
                    val recordFragment = RecordFragment.newInstance()
                    mFragment = recordFragment
                    openFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_lesson -> {
                    val lessonFragment = LessonFragment.newInstance()
                    mFragment = lessonFragment
                    openFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_setting -> {
                    val settingFragment = SettingFragment.newInstance()
                    mFragment = settingFragment
                    openFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun openFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_view, mFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
