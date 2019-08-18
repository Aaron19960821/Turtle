package com.turtle.yucwang.turtle

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.turtle.yucwang.turtle.AppUsage.AppUsageManager
import com.turtle.yucwang.turtle.Data.DailyUsage
import com.turtle.yucwang.turtle.Utils.StringUtils

class TurtleForegroundService: Service() {

    companion object {
        private const val TAG = "TurtleForegroundService"

        const val ACTION_START_SERVICE = "start_service"
        const val ACTION_UPDATE_USAGE = "update_usage"
    }

    private lateinit var mAppUsageManager: AppUsageManager
    private var mHasStarted: Boolean = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Turtle foreground service created.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Turtle foreground service started.")

        if (intent != null) {
            val action = intent.action
            if (action.equals(ACTION_START_SERVICE)) {
                mAppUsageManager = AppUsageManager.getInstance(this)
                startForegroundService()
                updateAppUsage()
                scheduleAlarmTask()
                mHasStarted = true
            } else if (action.equals(ACTION_UPDATE_USAGE)) {
                updateAppUsage()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.i(TAG, "Turtle foreground service destroyed")
        Toast.makeText(this,
                getText(R.string.foreground_service_stopped_toast), Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    private fun scheduleAlarmTask() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentTimeStamp = System.currentTimeMillis()
        val startTime = currentTimeStamp - (currentTimeStamp % (60 * 60 * 1000))
        val intent = Intent(this, TurtleBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC, startTime, 60 * 60 * 1000, pendingIntent)
    }

    private fun updateAppUsage() {
        if (mHasStarted) {
            mAppUsageManager.updateAppUsage(object : AppUsageManager.OnAppUsageUpdatedListener {
                override fun onAppUsageUpdated(currentDayData: DailyUsage) {
                    updateNotification(currentDayData)
                }
            })
        }
    }

    private fun updateNotification(dailyUsage: DailyUsage) {
        val notificationManager = NotificationManagerCompat.from(this)

        val notification = createNotification(dailyUsage, false)

        notificationManager.notify(1, notification)
    }

    private fun createNotification(dailyUsage: DailyUsage?, haveSound: Boolean): Notification {
        val notificationBuilder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel()
            notificationBuilder = NotificationCompat.Builder(this, getString(R.string.app_name))
        } else {
            notificationBuilder = NotificationCompat.Builder(this)
        }

        var notificationContent: String
        if (dailyUsage == null) {
            notificationContent = "Calculating..."
        } else {
            notificationContent = getString(R.string.notification_explanation) +
                    StringUtils.convertMillsecondsToString(this, dailyUsage!!.usage)
        }

        notificationBuilder.apply {
            setSmallIcon(R.drawable.turtle_app_icon)
            setContentText(notificationContent)
            if (!haveSound) setSound(null)
        }

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = getString(R.string.app_name)
            val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelId
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        val notification = createNotification(null, true)
        startForeground(1, notification)
    }
}