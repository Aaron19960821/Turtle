package com.turtle.yucwang.turtle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yucwang.turtle.Adapter.DailyUsageListApapter
import com.turtle.yucwang.turtle.AppUsage.AppUsageUtils
import com.turtle.yucwang.turtle.Data.DailyUsage
import com.turtle.yucwang.turtle.Utils.StringUtils
import com.turtle.yucwang.turtle.ViewModel.DailyUsageViewModel

import com.turtle.yucwang.turtle.R;

class DailyUsageListFragment : Fragment() {
    private lateinit var viewModel: DailyUsageViewModel

    private lateinit var dailyUsageList: RecyclerView
    private lateinit var dailyUsageListAdapter: DailyUsageListApapter
    private lateinit var dailyUsageListViewManger: RecyclerView.LayoutManager

    private lateinit var timeDisplay: TextView

    interface OnDailyUsageItemClickedListener {
        fun onClick(dailyUsage: DailyUsage)
    }

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dailyusage_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeDisplay = view.findViewById(R.id.today_daily_usage)

        dailyUsageListViewManger = LinearLayoutManager(context)
        dailyUsageListAdapter = DailyUsageListApapter(context, object : OnDailyUsageItemClickedListener {
            override fun onClick(dailyUsage: DailyUsage) {
                onDailyUsageItemSelected(dailyUsage)
            }
        })
        dailyUsageList = view.findViewById(R.id.daily_usages_list)
        dailyUsageList.apply {
            adapter = dailyUsageListAdapter
            layoutManager = dailyUsageListViewManger
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        viewModel = ViewModelProviders.of(this).get(DailyUsageViewModel::class.java)
        viewModel.init(context!!)
        viewModel.getAllDailyUsages().observe(this, Observer {
            dailyUsageListAdapter.setData(it)
        })
        viewModel.getTodayDailyUsage().observe(this, Observer {
            timeDisplay.apply {
                text = StringUtils.convertMillsecondsToString(context, it.usage)
                setTextColor(if (AppUsageUtils.isAppUsageAlert(it.usage)) context.getColor(R.color.colorAlert)
                else context.getColor(R.color.colorGood))
            }
        })

        val preferenceButton = view.findViewById<ImageButton>(R.id.turtle_preference)
        preferenceButton.setOnClickListener {
            findNavController().navigate(R.id.from_dailyusagelist_to_preference)
        }
    }

    private fun onDailyUsageItemSelected(dailyUsage: DailyUsage) {
        val bundle = bundleOf(
                Pair("app_usages_json_string", dailyUsage.description),
                Pair("date", dailyUsage.date)
        )
        findNavController().navigate(R.id.from_dailyusagelist_to_appusagelist, bundle)
    }
}