package com.yucwang.turtle.Services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import com.yucwang.turtle.Backend.AppUsageManager
import com.yucwang.turtle.Overview.HistoryListDatabase
import com.yucwang.turtle.Overview.OverviewHistoryListItem
import com.yucwang.turtle.R
import java.util.*

/**
 * The turtle class, used to monitor the current usage of android devices
 */
class TurtleService : Service() {
    companion object {
        val INVOKER_NAME = "invoker_name"
        val NORMAL_BROADCAST = "normal_broadcast_sync"
        val CALL_FROM_MAIN_ACTIVITY = "main_activity"
    }

    private lateinit var mNotifivationManager : NotificationManager
    var mTurtleServiceCallback : TurtleService.TurtleServiceCallback? = null
    private val mBinder : TurtleServiceBinder = TurtleServiceBinder(this)
    private var mTriggerIndex : Int = 0

    interface TurtleServiceCallback {
        fun onTaskFinished()
    }

    class TurtleServiceBinder(val turtleService: TurtleService) : Binder() { }

    override fun onCreate() {
        mNotifivationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        runTask(true)
        startRepeatingAppUsageSync()
        runTask(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val invokerName : String? = intent!!.getStringExtra(INVOKER_NAME)
        var isImportant = false
        var shouldSendDailyNotification = false
        if (invokerName != null && invokerName.equals(NORMAL_BROADCAST)) {
            mTriggerIndex = (mTriggerIndex + 1) % 24
            isImportant = (mTriggerIndex % 2 == 0)
            shouldSendDailyNotification = (mTriggerIndex == 12)
        } else if (invokerName != null && invokerName.equals(CALL_FROM_MAIN_ACTIVITY)) {
            isImportant = true
        }

        runTask(isImportant)

        if (shouldSendDailyNotification) {
            pushDailyNotification()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * We will update the device usage once an hour
     */
    private fun startRepeatingAppUsageSync() {
        var currentTime = System.currentTimeMillis()
        currentTime = currentTime - (currentTime % (60 * 60 * 1000))
        val intent = Intent(this as Context, TurtleBroadcastReceiver::class.java)
        val alarmRunning = (PendingIntent.getBroadcast(this as Context, 0, intent, PendingIntent.FLAG_NO_CREATE) != null)
        if (!alarmRunning) {
            val pendingIntent = PendingIntent.getBroadcast(this as Context, 0, intent, 0)
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, currentTime, 60 * 60 * 1000, pendingIntent)
        }
    }

    override fun onBind(intent : Intent) : IBinder? {
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        sendOnDestroyNotification()
    }

    fun pushDailyNotification() { }

    /**
     * Send the on destroy notification to user to inform them
     * to restart Turtle
     */
    fun sendOnDestroyNotification() {
        val notificationTitle = getString(R.string.turtle_service_on_destroy_title)
        val notificationBody = getString(R.string.turtle_service_on_destroy_message)

        val notification : Notification = Notification.Builder(this).setSmallIcon(R.mipmap.turtle)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .build()
        mNotifivationManager.notify(R.string.turtle_service_on_destroy_title, notification)
    }

    fun runTask(isImportant : Boolean) {
        AppUsageUpdateTask().execute(isImportant)
    }

    inner class AppUsageUpdateTask() : AsyncTask<Boolean, Void?, Void?>() {
        override fun doInBackground(vararg params: Boolean?) : Void? {
            val usageInInt = AppUsageManager.getInstance().getCurrentDayAppUsage(this@TurtleService as Context)
            // when the task is important, sync with database
            val historyListItem = OverviewHistoryListItem(Date(), usageInInt)

            // Update today's APP usage
            val database = HistoryListDatabase(this@TurtleService as Context)
            database.insertHistoryList(historyListItem)
            database.close()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (mTurtleServiceCallback != null) {
                mTurtleServiceCallback!!.onTaskFinished()
            }
        }
    }
}