package com.turtle.yucwang.turtle

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.turtle.yucwang.turtle.AppUsage.AppUsageManager

class TurtleActivity : AppCompatActivity() {

    private lateinit var mAppUsageManager: AppUsageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turtle)

        mAppUsageManager = AppUsageManager(this)
        mAppUsageManager.updateAppUsage()

        window.decorView.rootView.apply {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onBackPressed() {
        findNavController(R.id.navigation_host_fragment).navigateUp()
    }
}
