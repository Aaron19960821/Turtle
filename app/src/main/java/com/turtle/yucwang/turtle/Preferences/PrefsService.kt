package com.turtle.yucwang.turtle.Preferences

import android.content.Context
import java.util.concurrent.TimeUnit

class PrefsService private constructor(val context: Context) {

    fun putLong(key: String, value: Long) {
        val sharePreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharePreference.edit().putLong(key, value).apply()
    }

    fun getLong(key: String): Long {
        val sharePreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharePreference.getLong(key, -1)
    }

    fun getPhoneUsageLimit(): Long {
        val usage = getLong(Preferences.PHONE_USAGE.key)
        if (usage < 0) {
            return TimeUnit.HOURS.toMillis(DEFAULT_PHONE_USAGE_LIMIT_IN_MILLS)
        }
        return usage
    }

    fun getAppUsageLimit(): Long {
        val usage = getLong(Preferences.APP_USAGE.key)
        if (usage < 0) {
            return TimeUnit.HOURS.toMillis(DEFAULT_APP_USAGE_LIMIT_IN_MILLS)
        }
        return usage
    }

    companion object {

        enum class Preferences(val key: String) {
            PHONE_USAGE("pref_phone_usage"),
            APP_USAGE("pref_app_usage")
        }

        const val PREF_NAME = "Turtle"
        const val DEFAULT_PHONE_USAGE_LIMIT_IN_MILLS = 4L
        const val DEFAULT_APP_USAGE_LIMIT_IN_MILLS = 2L

        @Volatile private var instance: PrefsService? = null

        fun getInstance(context: Context): PrefsService {
            if (instance == null) {
                synchronized(this) {
                    instance = PrefsService(context.applicationContext)
                    return instance!!
                }
            } else {
                return instance!!
            }
        }
    }
}