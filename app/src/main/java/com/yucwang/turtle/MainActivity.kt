package com.yucwang.turtle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar

class MainActivity : AppCompatActivity(){

    lateinit var mBottomNavigationView: BottomNavigationView
    lateinit var mToolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = supportActionBar!!
        initBottomNavigationView()
    }

    // init the bottom navigation view
    private fun initBottomNavigationView() {
        mBottomNavigationView = this.findViewById(R.id.app_bottombar)
        mBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottombar_overview -> {
                    mToolbar.title = "Overview"
                    val overviewFragment = OverviewFragment.newInstance()
                    openFragment(overviewFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_record -> {
                    mToolbar.title = "Record"
                    val recordFragment = RecordFragment.newInstance()
                    openFragment(recordFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_lesson -> {
                    mToolbar.title = "Lesson"
                    val lessonFragment = LessonFragment.newInstance()
                    openFragment(lessonFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottombar_setting -> {
                    mToolbar.title = "Setting"
                    val settingFragment = SettingFragment.newInstance()
                    openFragment(settingFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_view, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
