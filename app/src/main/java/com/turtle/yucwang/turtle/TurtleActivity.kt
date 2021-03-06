package com.turtle.yucwang.turtle

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

class TurtleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turtle)

        window.decorView.rootView.apply {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        startTurtleService()
    }

    override fun onBackPressed() {
        findNavController(R.id.navigation_host_fragment).navigateUp()
    }

    private fun startTurtleService() {
        val intent = Intent(this, TurtleForegroundService::class.java)
        intent.action = TurtleForegroundService.ACTION_START_SERVICE
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}
