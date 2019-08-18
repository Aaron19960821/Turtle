package com.turtle.yucwang.turtle

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.turtle.yucwang.turtle.AppUsage.AppUsageManager

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
