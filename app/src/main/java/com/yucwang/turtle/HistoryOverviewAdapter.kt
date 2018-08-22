package com.yucwang.turtle

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class HistoryOverviewAdapter (private val historyList: Array<String>):
        RecyclerView.Adapter<HistoryOverviewAdapter.DataHolder>() {
    class DataHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val textView = LayoutInflater.from(parent.context).inflate()
    }
}