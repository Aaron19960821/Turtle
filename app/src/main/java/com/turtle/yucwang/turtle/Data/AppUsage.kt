package com.turtle.yucwang.turtle.Data

import android.graphics.drawable.Drawable

data class AppUsage(val appPackageName: String,
                    val appUsage: Long,
                    val icon: Drawable?) {
    override fun toString() = appPackageName
}