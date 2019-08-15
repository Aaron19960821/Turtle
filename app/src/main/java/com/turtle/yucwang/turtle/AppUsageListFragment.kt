package com.turtle.yucwang.turtle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yucwang.turtle.Adapter.AppUsageListAdapter
import com.turtle.yucwang.turtle.ViewModel.AppUsageViewModel

class AppUsageListFragment : Fragment() {
    private lateinit var mViewModel: AppUsageViewModel

    private lateinit var mAppUsageList: RecyclerView
    private lateinit var mAppUsageListAdapter: AppUsageListAdapter
    private lateinit var mAppUsageListLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.appusage_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAppUsageListLayoutManager = LinearLayoutManager(context)

        mAppUsageListAdapter = AppUsageListAdapter(context)
        mAppUsageList = view.findViewById(R.id.appusage_list)
        mAppUsageList.apply {
            adapter = mAppUsageListAdapter
            layoutManager = mAppUsageListLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        mViewModel = ViewModelProviders.of(this).get(AppUsageViewModel::class.java)
        mViewModel.init(context!!, arguments!!.getString("app_usages_json_string"))
        mViewModel.getAppUsages().observe(this, Observer {
            mAppUsageListAdapter.setData(it)
        })
    }
}