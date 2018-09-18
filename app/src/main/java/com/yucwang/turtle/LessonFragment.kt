package com.yucwang.turtle


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yucwang.turtle.Theme.SystemUiUtils
import com.yucwang.turtle.Theme.ThemeInterface

/**
 * The lesson fragment, currently no lesson
 * is here.
 */
class LessonFragment : Fragment(), ThemeInterface {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        updateSystemUi()

        return inflater.inflate(R.layout.lesson_main, null)
    }

    override fun getNavigationBarColor(): Int {
        return resources.getColor(R.color.colorPass)
    }

    override fun getStatusBarColor(): Int {
        return resources.getColor(R.color.colorPass)
    }

    override fun updateSystemUi() {
        val navigationBarColor = getNavigationBarColor()
        SystemUiUtils.setNavigationBarColor(activity!!.window, navigationBarColor)
        SystemUiUtils.setNavigationBarIconColor(activity!!.window, navigationBarColor)

        val statusBarColor = getStatusBarColor()
        SystemUiUtils.setStatusBarColor(activity!!.window, statusBarColor)
        SystemUiUtils.setStatusBarIconColor(activity!!.window, statusBarColor)
    }

    companion object {
        fun newInstance(): LessonFragment = LessonFragment()
    }
}
