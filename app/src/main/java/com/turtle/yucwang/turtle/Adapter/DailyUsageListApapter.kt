package com.turtle.yucwang.turtle.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yucwang.turtle.AppUsage.AppUsageUtils
import com.turtle.yucwang.turtle.DailyUsageListFragment
import com.turtle.yucwang.turtle.Data.DailyUsage
import com.turtle.yucwang.turtle.R
import com.turtle.yucwang.turtle.Utils.StringUtils
import com.turtle.yucwang.turtle.ViewModel.DailyUsageViewModel

class DailyUsageListApapter(val context: Context?,
                            val listener: DailyUsageListFragment.OnDailyUsageItemClickedListener) :
        RecyclerView.Adapter<DailyUsageListApapter.DailyUsageListViewHolder>() {

    private var dailyUsages: List<DailyUsage>? = null

    class DailyUsageListViewHolder(val view: View,
                                   val listener: DailyUsageListFragment.OnDailyUsageItemClickedListener,
                                   val dateTextView: TextView = view.findViewById(R.id.date),
                                   val usageTextView: TextView = view.findViewById(R.id.usage))
        : RecyclerView.ViewHolder(view) {
        fun bind(dailyUsage: DailyUsage?) {
            if (dailyUsage != null) {
                dateTextView.apply {
                    text = dailyUsage.date
                    setTextColor(if (AppUsageUtils.isAppUsageAlert(dailyUsage.usage))
                        context.getColor(R.color.colorAlert)
                    else context.getColor(R.color.colorGood))
                }
                usageTextView.apply {
                    text = StringUtils.convertMillsecondsToString(dailyUsage.usage)
                    setTextColor(if (AppUsageUtils.isAppUsageAlert(dailyUsage.usage))
                        context.getColor(R.color.colorAlert)
                    else context.getColor(R.color.colorGood))
                }

                view.setOnClickListener({listener.onClick(dailyUsage)})
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyUsageListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.dailyusage_list_item, parent, false)
        return DailyUsageListViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: DailyUsageListViewHolder, position: Int) {
        holder.bind(getDataAt(position))
    }

    override fun getItemCount() = if (dailyUsages == null) 0 else dailyUsages!!.size

    fun setData(dailyUsages: List<DailyUsage>) {
        this.dailyUsages = dailyUsages
        notifyDataSetChanged()
    }

    private fun getDataAt(position: Int) = dailyUsages!!.get(position)
}