package com.turtle.yucwang.turtle.Preferences

import android.app.Dialog
import android.os.Bundle
import com.takisoft.preferencex.TimePickerPreference
import com.takisoft.preferencex.TimePickerPreferenceDialogFragmentCompat
import java.util.concurrent.TimeUnit

class TurtleTimePreferenceDialogFragmentCompat: TimePickerPreferenceDialogFragmentCompat() {

    private lateinit var mPreference: TimePickerPreference

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (preference is TimePickerPreference) {
            mPreference = preference as TimePickerPreference

            mPreference.apply {
                dialogTitle = mPreference.title
            }
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)

        if (positiveResult) {
            val hourOfDay = mPreference.hourOfDay
            val minute = mPreference.minute
            val timeInMills = TimeUnit.HOURS.toMillis(hourOfDay.toLong()) + TimeUnit.MINUTES.toMillis(minute.toLong())
            PrefsService.getInstance(context!!).putLong(mPreference.key, timeInMills)
        }
    }

    companion object {
        fun newInstance(key: String): TurtleTimePreferenceDialogFragmentCompat {
            val fragment = TurtleTimePreferenceDialogFragmentCompat()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }
}