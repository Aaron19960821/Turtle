package com.yucwang.turtle.Theme

interface ThemeInterface {
    /**
     * Get the status bar color, only be inherited by Activity
     */
    fun getStatusBarColor(): Int

    /**
     * Get the navigation bar color, only be inherited by Activity
     */
    fun getNavigationBarColor(): Int

    /**
     * Update the system UI (Navigation bar and status bar)
     */
    fun updateSystemUi()
}