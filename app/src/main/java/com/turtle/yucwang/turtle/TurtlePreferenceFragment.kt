package com.turtle.yucwang.turtle

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.takisoft.preferencex.TimePickerPreference
import com.turtle.yucwang.turtle.Preferences.PrefsService
import com.turtle.yucwang.turtle.Preferences.TurtleTimePreferenceDialogFragmentCompat
import java.util.concurrent.TimeUnit

class TurtlePreferenceFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.turtle_main_preference, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.preferece_toolbar)
        toolbar.apply {
            title = view.context.getString(R.string.preferences)
            setNavigationIcon(R.drawable.ic_left_arrow)
            setNavigationOnClickListener {
                activity!!.onBackPressed()
            }
        }
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if (preference is TimePickerPreference) {
            val preferenceDialog = TurtleTimePreferenceDialogFragmentCompat.newInstance(preference.key)
            preferenceDialog.setTargetFragment(this, 0)
            preferenceDialog.show(fragmentManager!!, TurtleTimePreferenceDialogFragmentCompat::class.java.name)

            var timeInMills: Long = -1
            if (preference.key.equals(PrefsService.Companion.Preferences.APP_USAGE.key)) {
                timeInMills = PrefsService.getInstance(context!!).getAppUsageLimit()
            } else if (preference.key.equals(PrefsService.Companion.Preferences.PHONE_USAGE.key)) {
                timeInMills = PrefsService.getInstance(context!!).getPhoneUsageLimit()
            }

            if (timeInMills >= 0) {
                val hours = TimeUnit.MILLISECONDS.toHours(timeInMills).toInt()
                val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMills).toInt() % TimeUnit.HOURS.toMinutes(1).toInt()
                preference.setTime(hours, minutes)
            }
        } else {
            return super.onDisplayPreferenceDialog(preference)
        }
    }
}