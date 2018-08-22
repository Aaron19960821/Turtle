package com.yucwang.turtle


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OverviewFragment : Fragment() {
    private lateinit var mHistoryOverview: RecyclerView
    private lateinit var mHistoryOverviewAdapter: RecyclerView.Adapter<*>
    private lateinit var mHistoryOverviewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.overview_main, null)
    }

    companion object {
        fun newInstance(): OverviewFragment = OverviewFragment()
    }
}
