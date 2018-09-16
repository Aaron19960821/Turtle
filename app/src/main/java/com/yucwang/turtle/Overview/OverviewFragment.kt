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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * The overview fragment
 * Created by Yuchen Wong
 */
class OverviewFragment() : Fragment() {

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

        return mContentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareData()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initTimeDisplay() {
        assert(mHistoryListData.size > 0)
        mTimeDisplayTextEdit.text = mHistoryListData[0].getFormattedUsage()

        if (OverviewHistoryListItem.isAlertTimeUsage(mHistoryListData[0].mTimeUsage)) {
            mTimeDisplay.setBackgroundColor(resources.getColor(R.color.colorAlert))
        } else {
            mTimeDisplay.setBackgroundColor(resources.getColor(R.color.colorPass))
        }
    }

    /**
     * Fetch All Data From the database
     */
    private fun prepareData() {
        val database = HistoryListDatabase(context!!)
        mHistoryListData = database.getAllHistoryList()
        database.close()
    }

    companion object {
        fun newInstance(): OverviewFragment = OverviewFragment()

        private val TAG = "OverviewFragment"
    }
}
