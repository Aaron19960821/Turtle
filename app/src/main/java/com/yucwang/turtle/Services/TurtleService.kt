package com.yucwang.turtle.Services

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
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

class TurtleService : Service() {

    private lateinit var mNotifivationManager : NotificationManager
    var mTurtleServiceCallback : TurtleService.TurtleServiceCallback? = null
    private val mBinder : TurtleServiceBinder = TurtleServiceBinder(this)

    interface TurtleServiceCallback {
        fun onTaskFinished()
    }

    class TurtleServiceBinder(val turtleService: TurtleService) : Binder() { }

    override fun onCreate() {
        mNotifivationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        runTask(true)
    }

    override fun onBind(intent : Intent) : IBinder? {
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        sendOnDestroyNotification()
    }

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
            if (params[0]!!) {
                val historyListItem = OverviewHistoryListItem(Date(), usageInInt)

                // Update today's APP usage
                val database = HistoryListDatabase(this@TurtleService as Context)
                database.insertHistoryList(historyListItem)
                database.close()
            }
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