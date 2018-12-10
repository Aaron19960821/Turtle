package com.yucwang.turtle.Backend

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.yucwang.turtle.TurtleConstants
import com.yucwang.turtle.Utils.AppIconUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppUsageManager(val context: Context) {
    private val TAG = "AppUsageManager"

    companion object {
        fun getStartOfCurrentDay(): Calendar {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.HOUR, -Calendar.HOUR_OF_DAY)
            calendar.add(Calendar.MINUTE, -Calendar.MINUTE)
            calendar.add(Calendar.SECOND, -Calendar.SECOND)

            return calendar
        }

        private val PREF_YESTERDAY = "yesterday"
        private val PREF_TODAY = "today"
        private val PREF_MOST_USED_APP = "most_used_app"
        private val PREF_MOST_USED_APP_TIME = "most_used_app_time"
        private val PREF_DATE = "date"
    }

    var mAppUsageList: ArrayList<AppUsage> = ArrayList<AppUsage>()

    fun updateCurrentDayAppUsage() {
        if (context == null) return

        val mUsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val start = getStartOfCurrentDay().timeInMillis
        val end = System.currentTimeMillis()
        val usageList = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, start, end)

        for (usageStates in usageList) {
            val packageName = usageStates.packageName
            var haveFound = false
            for (appUsage in mAppUsageList) {
                if (appUsage.mPackageName.equals(packageName)) {
                    appUsage.mUsage = usageStates.totalTimeInForeground
                    haveFound = true
                    break
                }
            }

            // App may be uninstalled or installed during the day
            if (!haveFound) {
                mAppUsageList.add(AppUsage(usageStates.packageName, usageStates.totalTimeInForeground))
            }
        }

        // get the most used app
        var mostUsedApp : AppUsage = AppUsage("", 0L)
        for (appUsage in mAppUsageList) {
            if (appUsage.mUsage > mostUsedApp.mUsage) {
                mostUsedApp = appUsage
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time)
        val currentTimeStamp = context.getSharedPreferences(PREF_TODAY, Context.MODE_PRIVATE).getString(PREF_DATE, timeStamp);

        if (!timeStamp.equals(currentTimeStamp)) {
            moveTodayPreferenceToYesterday()
        }

        context.getSharedPreferences(PREF_TODAY, Context.MODE_PRIVATE).edit().putString(PREF_DATE, timeStamp).apply()
        context.getSharedPreferences(PREF_TODAY, Context.MODE_PRIVATE).edit().putString(PREF_MOST_USED_APP, mostUsedApp.mPackageName).apply()
        context.getSharedPreferences(PREF_TODAY, Context.MODE_PRIVATE).edit().putLong(PREF_MOST_USED_APP_TIME, mostUsedApp.mUsage).apply()

        // save the most used app information to the Turtle directory
        try {
            val mostUsedAppIcon = context.packageManager.getApplicationIcon(mostUsedApp.mPackageName)
            val mostUsedAppIconBitmap = AppIconUtils.cvtDrawableToBitmap(mostUsedAppIcon)
            AppIconUtils.saveBitMapToFile(context, mostUsedAppIconBitmap, TurtleConstants.MOST_USED_APP_TODAY, Bitmap.CompressFormat.PNG, 100)
        } catch(e : PackageManager.NameNotFoundException) {
            Log.d (TAG, "can not find most used app package")
        } catch(e : Exception) {
            Log.d(TAG, "fail to save the app icon")
        }
    }

    /**
     * Migrate current day's profile to the last day
     */
    private fun moveTodayPreferenceToYesterday() {
        val timeStamp = context.getSharedPreferences(PREF_TODAY, Context.MODE_PRIVATE).getString(PREF_DATE, "")
        val mostUsedAppName = context.getSharedPreferences(PREF_TODAY, Context.MODE_PRIVATE).getString(PREF_MOST_USED_APP, "")
        val mostUsedAppTime = context.getSharedPreferences(PREF_TODAY, Context.MODE_PRIVATE).getLong(PREF_MOST_USED_APP_TIME, 0L)

        if (!timeStamp.equals("")) {
            context.getSharedPreferences(PREF_YESTERDAY, Context.MODE_PRIVATE).edit().putString(PREF_DATE, timeStamp).apply()
            context.getSharedPreferences(PREF_YESTERDAY, Context.MODE_PRIVATE).edit().putString(PREF_MOST_USED_APP, mostUsedAppName).apply()
            context.getSharedPreferences(PREF_YESTERDAY, Context.MODE_PRIVATE).edit().putLong(PREF_MOST_USED_APP_TIME, mostUsedAppTime).apply()
            AppIconUtils.renameAppIconFile(context, TurtleConstants.MOST_USED_APP_TODAY, TurtleConstants.MOST_USED_APP_YESTERDAY)
        }
    }

    /**
     * Get the Usage of Current Day.
     */
    fun getCurrentDayAppUsage(): Long {
        Log.d(TAG, "Start getting app usage for the current day.")

        updateCurrentDayAppUsage()

        var totalTime = 0L
        for (appUsage in mAppUsageList) {
            totalTime += appUsage.mUsage
        }

        return totalTime
    }

}