package com.yucwang.turtle

import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.app.Application
import android.os.Process
import android.os.SystemClock
import android.provider.Settings
import android.widget.Toast
import com.yucwang.turtle.Services.TurtleService

class TurtleApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}