package com.turtle.yucwang.turtle

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.turtle.yucwang.turtle.AppUsage.AppUsageManager

class TurtleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turtle)

        val appUsageManager = AppUsageManager(this as Context)
        appUsageManager.updateAppUsage()
    }
}
