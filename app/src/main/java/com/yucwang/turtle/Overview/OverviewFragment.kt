package com.yucwang.turtle.Overview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.Time
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yucwang.turtle.Backend.AppUsageManager
import com.yucwang.turtle.R
import com.yucwang.turtle.Theme.SystemUiUtils
import com.yucwang.turtle.Theme.ThemeInterface
import com.yucwang.turtle.TurtleFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/**
 * The overview fragment
 * Created by Yuchen Wong
 */
class OverviewFragment() : TurtleFragment(), ThemeInterface {

    private lateinit var mHistoryList: RecyclerView
    private lateinit var mHistoryListAdapter: HistoryListAdapter
    private lateinit var mHistoryListManager: RecyclerView.LayoutManager
    private lateinit var mContentView: View
    private lateinit var mHistoryListData: Array<OverviewHistoryListItem>
    private lateinit var mTimeDisplay: View
    private lateinit var mTimeDisplayTextEdit: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d (TAG, "on_created")
        mContentView = inflater.inflate(R.layout.overview_main, null) as View

        mTimeDisplay = mContentView.findViewById(R.id.overview_timedisplay)
        mTimeDisplayTextEdit = mTimeDisplay.findViewById(R.id.timedisplay)
        initTimeDisplay()

        mHistoryListManager = LinearLayoutManager(activity)
        mHistoryListAdapter = HistoryListAdapter(mHistoryListData, context!!)
        mHistoryList = mContentView.findViewById<RecyclerView>(R.id.overview_historylist).apply {
            setHasFixedSize(true)
            layoutManager = mHistoryListManager
            adapter = mHistoryListAdapter
        }

        updateSystemUi()

        return mContentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareData()
    }

    override fun onResume() {
        super.onResume()

        updateSystemUi()
    }

    override fun updateSystemUi() {
        val navigationBarColor = getNavigationBarColor()
        SystemUiUtils.setNavigationBarColor(activity!!.window, navigationBarColor)
        SystemUiUtils.setNavigationBarIconColor(activity!!.window, navigationBarColor)

        val statusBarColor = getStatusBarColor()
        SystemUiUtils.setStatusBarColor(activity!!.window, statusBarColor)
        SystemUiUtils.setStatusBarIconColor(activity!!.window, statusBarColor)
    }

    override fun getNavigationBarColor(): Int {
        var navigationBarColor: Int = resources.getColor(R.color.colorPass)
        if (mHistoryListData.size > 0 && OverviewHistoryListItem.isAlertTimeUsage(mHistoryListData[0].mTimeUsage)) {
            navigationBarColor = resources.getColor(R.color.colorAlert)
        }
        return navigationBarColor
    }

    override fun getStatusBarColor(): Int {
        var statusBarColor: Int = resources.getColor(R.color.colorPass)
        if (mHistoryListData.size > 0 && OverviewHistoryListItem.isAlertTimeUsage(mHistoryListData[0].mTimeUsage)) {
            statusBarColor = resources.getColor(R.color.colorAlert)
        }
        return statusBarColor
    }

    private fun initTimeDisplay() {
        assert(mHistoryListData.size > 0)
        if (mHistoryListData.size > 0) {
            mTimeDisplayTextEdit.text = mHistoryListData[0].getFormattedUsage()

            if (OverviewHistoryListItem.isAlertTimeUsage(mHistoryListData[0].mTimeUsage)) {
                mTimeDisplay.setBackgroundColor(resources.getColor(R.color.colorAlert))
            } else {
                mTimeDisplay.setBackgroundColor(resources.getColor(R.color.colorPass))
            }
        } else {
            mTimeDisplay.setBackgroundColor(resources.getColor(R.color.colorPass));
        }
    }

    /**
     * Fetch All Data From the database and get it sorted.
     */
    private fun prepareData() {
        val database = HistoryListDatabase(context!!)
        mHistoryListData = database.getAllHistoryList()
        mHistoryListData.sortWith(object: Comparator<OverviewHistoryListItem> {
            override fun compare(o1: OverviewHistoryListItem, o2: OverviewHistoryListItem): Int = when {
                o1.encodeDateForDataBase() > o2.encodeDateForDataBase() -> 1
                else -> 0
            }
        })
        database.close()
    }

    override fun onAppUsageDataChanged() {
        super.onAppUsageDataChanged()
        prepareData()
        if (mHistoryListData.size > 0) {
            mTimeDisplayTextEdit.text = mHistoryListData[0].getFormattedUsage()
        }
        mHistoryListAdapter.mHistoryList = mHistoryListData
        mHistoryListAdapter.notifyDataSetChanged()
        updateSystemUi()
    }

    companion object {
        fun newInstance(): OverviewFragment = OverviewFragment()

        private val TAG = "OverviewFragment"
    }
}
