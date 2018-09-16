package com.yucwang.turtle


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * The lesson fragment, currently no lesson
 * is here.
 */
class LessonFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.lesson_main, null)
    }

    companion object {
        fun newInstance(): LessonFragment = LessonFragment()
    }
}
