package com.turtle.yucwang.turtle

import android.os.Bundle
import android.preference.MultiSelectListPreference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.preference.MultiSelectListPreferenceDialogFragmentCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

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
        val preferenceDialog = MultiSelectListPreferenceDialogFragmentCompat.newInstance(preference!!.key)
        preferenceDialog.setTargetFragment(this, 0)
        preferenceDialog.show(fragmentManager!!, "Show")
    }
}