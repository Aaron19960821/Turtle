package com.turtle.yucwang.turtle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast

class TurtleBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, TurtleForegroundService::class.java)
        intent.action = TurtleForegroundService.ACTION_UPDATE_USAGE
        if (Build.VERSION.SDK_INT >= 26) {
            context!!.startForegroundService(intent)
        } else {
            context!!.startService(intent)
        }

        Toast.makeText(context, "Turtle received", Toast.LENGTH_LONG).show()
    }
}