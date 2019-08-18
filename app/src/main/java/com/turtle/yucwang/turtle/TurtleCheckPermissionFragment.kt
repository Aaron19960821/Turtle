package com.turtle.yucwang.turtle

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.turtle.yucwang.turtle.AppUsage.AppUsageManager
import com.turtle.yucwang.turtle.Data.DailyUsage

class TurtleCheckPermissionFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activateTurtle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.check_permission_fragment, container, false)
        val activateTurtleButton = view.findViewById<Button>(R.id.activate_turtle_button)
        activateTurtleButton.setOnClickListener({
            activateTurtle()
        })

        return view
    }

    private fun activateTurtle() {
        if (!AppUsageManager.getInstance(activity!!).checkPermission()) {
            showAcquirePermissionDialog()
        } else {
            AppUsageManager.getInstance(activity!!).updateAppUsage(object : AppUsageManager.OnAppUsageUpdatedListener {
                override fun onAppUsageUpdated(currentDayData: DailyUsage) {
                    findNavController().navigate(R.id.from_check_permission_to_dailyusage)
                }
            })
        }
    }

    private fun showAcquirePermissionDialog() {
        val askPermissionDialogBuilder = AlertDialog.Builder(context)
                .setTitle(getText(R.string.ask_for_permission_dialog_title))
                .setMessage(getText(R.string.ask_for_permission_dialog_body))
                .setPositiveButton(getText(R.string.yes), {dialog, which -> startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))})
                .setNegativeButton(getText(R.string.cancel), {dialog, which ->  Toast.makeText(context, "not allowed!!", Toast.LENGTH_LONG)})

        askPermissionDialogBuilder.create().show()
    }
}