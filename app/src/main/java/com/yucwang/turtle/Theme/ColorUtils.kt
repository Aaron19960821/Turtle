package com.yucwang.turtle.Theme

import android.graphics.Color

class ColorUtils() {
    companion object {
        private val CONTRAST_LIGHT_ITEM_THRESHOLD = 3f

        private fun getLightnessForColor(color: Int): Float {
            val bgR = Color.red(color)
            val bgG = Color.green(color)
            val bgB = Color.blue(color)

            val largest = Math.max(bgR, Math.max(bgG, bgB))
            val smallest = Math.min(bgR, Math.min(bgG, bgB))
            val average = (largest + smallest) / 2

            return average.toFloat() / 255f
        }

        private fun getContrastForColor(color: Int): Float {
            var bgR = Color.red(color) / 255f
            var bgG = Color.green(color) / 255f
            var bgB = Color.blue(color) / 255f
            bgR = if (bgR < 0.03928f) bgR / 12.92f else Math.pow(((bgR + 0.055f) / 1.055f).toDouble(), 2.4).toFloat()
            bgG = if (bgG < 0.03928f) bgG / 12.92f else Math.pow(((bgG + 0.055f) / 1.055f).toDouble(), 2.4).toFloat()
            bgB = if (bgB < 0.03928f) bgB / 12.92f else Math.pow(((bgB + 0.055f) / 1.055f).toDouble(), 2.4).toFloat()
            val bgL = 0.2126f * bgR + 0.7152f * bgG + 0.0722f * bgB
            return Math.abs(1.05f / (bgL + 0.05f))
        }

        fun shouldUseLightForegroundOnBackground(backgroundColor: Int): Boolean {
            return getContrastForColor(backgroundColor) >= CONTRAST_LIGHT_ITEM_THRESHOLD
        }
    }
}