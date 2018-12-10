package com.yucwang.turtle.Utils

import android.app.AppOpsManager
import android.content.Context
import android.os.Process

class TurtleUtils() {
    companion object {
        /**
         * Get whether we have obtain the app usage permission
         */
        fun haveAppUsagePermission(context: Context) : Boolean {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
            return mode == AppOpsManager.MODE_ALLOWED
        }
    }
}