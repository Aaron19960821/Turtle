package com.turtle.yucwang.turtle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.turtle.yucwang.turtle.ViewModel.DailyUsageViewModel

class DailyUsageListFragment : Fragment() {
    private lateinit var viewModel: DailyUsageViewModel

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        viewModel = activity?.run {
            ViewModelProvider.NewInstanceFactory().create(DailyUsageViewModel::class.java)
        } ?: throw Exception("DailyUsageListFragment: Invalid activity, unable to create activity")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.dailyusage_list, container, false)

        return contentView
    }
}