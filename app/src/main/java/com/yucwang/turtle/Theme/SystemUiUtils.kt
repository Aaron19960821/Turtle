package com.yucwang.turtle.Theme

import android.graphics.Color
import android.view.View
import android.view.Window
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager

class SystemUiUtils() {
    companion object {
        fun setNavigationBarColor(window: Window, color: Int) {
            window.navigationBarColor = color
        }

        fun setNavigationBarIconColor(window: Window, color: Int) {
            val rootView = window.decorView.rootView
            val useLightColor = ColorUtils.shouldUseLightForegroundOnBackground(color)
            var visibility = rootView.systemUiVisibility

            if (useLightColor) {
                visibility = visibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                visibility = visibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            rootView.systemUiVisibility = visibility
        }

        /**
         * @see android.view.Window.setStatusBarColor
         */
        fun setStatusBarColor(window: Window, statusBarColor: Int) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            // This benefits battery usage on L and M.  However, this no longer provides a battery
            // benefit as of N and starts to cause flicker bugs on O, so don't bother on O and up.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && statusBarColor == Color.BLACK
                    && window.navigationBarColor == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            window.statusBarColor = statusBarColor
        }

        /**
         * Sets the status bar icons to dark or light. Note that this is only valid for
         * Android M+.
         *
         * @param rootView The root view used to request updates to the system UI theming.
         * @param useDarkIcons Whether the status bar icons should be dark.
         */
        fun setStatusBarIconColor(window: Window, color: Int) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

            val rootView = window.decorView.rootView as ViewGroup
            val useDarkIcons = ColorUtils.shouldUseLightForegroundOnBackground(color)

            var systemUiVisibility = rootView.systemUiVisibility
            if (useDarkIcons) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            rootView.systemUiVisibility = systemUiVisibility
        }
    }
}