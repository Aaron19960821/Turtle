package com.yucwang.turtle.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TurtleBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context!!, TurtleService::class.java)
        intent.putExtra(TurtleService.INVOKER_NAME, TurtleService.NORMAL_BROADCAST)
        context.startService(intent)
    }
}
