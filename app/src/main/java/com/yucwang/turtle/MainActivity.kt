package com.yucwang.turtle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar

class MainActivity : AppCompatActivity(){

    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mToolbar: ActionBar
    private lateinit var mFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = supportActionBar!!
        initBottomNavigationView()
        initFragment()
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
                    mToolbar.title = "Overview"
                    val overviewFragment = OverviewFragment.newInstance()
                    mFragment = overviewFragment
                    openFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_record -> {
                    mToolbar.title = "Record"
                    val recordFragment = RecordFragment.newInstance()
                    mFragment = recordFragment
                    openFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_lesson -> {
                    mToolbar.title = "Lesson"
                    val lessonFragment = LessonFragment.newInstance()
                    mFragment = lessonFragment
                    openFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_setting -> {
                    mToolbar.title = "Setting"
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
