package com.yucwang.turtle

import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.Toast
import com.yucwang.turtle.Backend.AppUsageManager
import com.yucwang.turtle.Lesson.LessonFragment
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
        acquirePermission()

        setContentView(R.layout.activity_main)
        mOverViewDataAdapter = AppUsageManager(this as Context)

        refreshAppUsage()

        initBottomNavigationView()
        initFragment()
    }

    override fun onResume() {
        super.onResume()
        refreshAppUsage()
    }

    private fun acquirePermission() {
        val appOps = (this as Context).getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), (this as Context).packageName)

        if (mode != AppOpsManager.MODE_ALLOWED) {
            val askPermissionDialogBuilder = AlertDialog.Builder(this@MainActivity)
            askPermissionDialogBuilder.setTitle("Ask for permission")
            askPermissionDialogBuilder.setMessage("Turtle need usage state permission to work properly, we will be unable to" +
                    " work of the permission is not granted.")
            askPermissionDialogBuilder.setPositiveButton("Yes"){dialog, which ->
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }
            askPermissionDialogBuilder.setNegativeButton("No") {dialog, which ->
                Toast.makeText(applicationContext, "Permission request denied.", Toast.LENGTH_SHORT).show()
            }

            val dialog = askPermissionDialogBuilder.create()
            dialog.show()
        }
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
        BottomNavigationViewHelper.removeShiftMode(mBottomNavigationView)
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
