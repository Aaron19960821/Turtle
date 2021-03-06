package com.turtle.yucwang.turtle.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yucwang.turtle.AppUsage.AppUsageUtils
import com.turtle.yucwang.turtle.Data.AppUsage
import com.turtle.yucwang.turtle.R
import com.turtle.yucwang.turtle.Utils.StringUtils

class AppUsageListAdapter(val context: Context?):
        RecyclerView.Adapter<AppUsageListAdapter.AppUsageListViewHolder>() {
    class AppUsageListViewHolder(val view: View,
                                 val appIconView: ImageView = view.findViewById(R.id.icon),
                                 val appNameView: TextView = view.findViewById(R.id.name),
                                 val appUsageView: TextView = view.findViewById(R.id.usage)) : RecyclerView.ViewHolder(view) {

        fun bind(appUsage: AppUsage?) {
            if (appUsage != null) {
                appNameView.apply {
                    text = appUsage.appPackageName
                }
                appIconView.apply {
                    setImageDrawable(if (appUsage.icon == null)
                        context.getDrawable(R.mipmap.ic_launcher_round)
                    else appUsage!!.icon!!)
                }
                appUsageView.apply {
                    text = StringUtils.convertMillsecondsToString(context, appUsage.appUsage)
                    setTextColor(if (AppUsageUtils.isAppUsageAlert(context, appUsage.appUsage))
                        context.getColor(R.color.colorAlert)
                    else context.getColor(R.color.colorGood))
                }
            }
        }
    }

    private var mData: List<AppUsage>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppUsageListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.appusage_list_item, parent, false)
        return AppUsageListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppUsageListViewHolder, position: Int) {
        holder.bind(getDataAt(position))
    }

    override fun getItemCount() = if (mData == null) 0 else mData!!.size

    fun setData(newDataset: List<AppUsage>) {
        mData = newDataset
        notifyDataSetChanged()
    }

    private fun getDataAt(index: Int): AppUsage? {
        if (mData == null || index >= mData!!.size) return null
        return mData!![index]
    }
}