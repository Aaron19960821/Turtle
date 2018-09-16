package com.yucwang.turtle.Overview

import android.content.Context
import android.support.annotation.Nullable
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yucwang.turtle.R
import org.w3c.dom.Text

class HistoryListAdapter(private val mHistoryList: Array<OverviewHistoryListItem>, private val mContext: Context):
        RecyclerView.Adapter<HistoryListAdapter.HistoryListViewHolder>() {

    class HistoryListViewHolder(val mItemView: View): RecyclerView.ViewHolder(mItemView) {
        val mBackgroundView = mItemView.findViewById(R.id.listitem_background) as View
        val mDateView = mItemView.findViewById(R.id.listitem_date) as TextView
        val mUsageView = mItemView.findViewById(R.id.listitem_usage) as TextView
        val mProgressBarView = mItemView.findViewById(R.id.listitem_progressbar) as View
    }

    // Create new recycler view list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryListViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.overview_historylistitem,
                parent, false)
        return HistoryListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryListViewHolder, position: Int) {
        holder.mDateView.text = mHistoryList.get(position).getFormattedDate()
        holder.mUsageView.text = mHistoryList.get(position).getFormattedUsage()

        if (OverviewHistoryListItem.isAlertTimeUsage(mHistoryList[position].mTimeUsage)) {
            holder.mBackgroundView.setBackgroundColor(holder.mBackgroundView.resources.getColor(R.color.colorAlert))
        } else {
            holder.mBackgroundView.setBackgroundColor(holder.mBackgroundView.resources.getColor(R.color.colorPass))
        }
    }

    // The number of history items.
    override fun getItemCount() = mHistoryList.size
}