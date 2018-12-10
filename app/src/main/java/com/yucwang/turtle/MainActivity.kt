package com.yucwang.turtle

import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.Toast
import com.yucwang.turtle.Lesson.LessonFragment
import com.yucwang.turtle.Overview.OverviewFragment
import com.yucwang.turtle.Services.TurtleService
import com.yucwang.turtle.Utils.TurtleUtils

class MainActivity : AppCompatActivity(), TurtleService.TurtleServiceCallback{

    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mFragment: TurtleFragment

    inner class TurtleServiceConnectionImpl() : ServiceConnection {
        private var isBound = false
        private lateinit var mService : TurtleService

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val turtleServiceBinder : TurtleService.TurtleServiceBinder = service as TurtleService.TurtleServiceBinder
            val turtleService = turtleServiceBinder.turtleService
            mService = turtleService
            turtleService.mTurtleServiceCallback = this@MainActivity
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

        fun disconnectService() {
            if (isBound) {
                isBound = false
                mService.mTurtleServiceCallback = null
                unbindService(this)
            }
        }
    }
    private val mTurtleServiceConnection : TurtleServiceConnectionImpl = TurtleServiceConnectionImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runOnUiThread(Runnable {
            acquirePermission()
            setContentView(R.layout.activity_main)
            initBottomNavigationView()
            initFragment()
        })
    }

    override fun onStart() {
        super.onStart()
        val intent : Intent = Intent(this, TurtleService::class.java)
        bindService(intent, mTurtleServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        mTurtleServiceConnection.disconnectService()
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, TurtleService::class.java)
        intent.putExtra(TurtleService.INVOKER_NAME, TurtleService.CALL_FROM_MAIN_ACTIVITY)
        startService(intent)
        mFragment.onResume()
    }

    private fun acquirePermission() {
        if (!TurtleUtils.haveAppUsagePermission(this@MainActivity as Context)) {
            val askPermissionDialogBuilder = AlertDialog.Builder(this@MainActivity)
            askPermissionDialogBuilder.setTitle("Ask for permission")
            askPermissionDialogBuilder.setMessage("Turtle need usage state permission to work properly, we will be unable to" +
                    " work if the permission is not granted.")
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
        transaction.replace(R.id.content_view, mFragment as Fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onTaskFinished() {
        mFragment.onAppUsageDataChanged()
    }
}
