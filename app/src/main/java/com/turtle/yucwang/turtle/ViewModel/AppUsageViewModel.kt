package com.turtle.yucwang.turtle.ViewModel

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.turtle.yucwang.turtle.Data.AppUsage
import org.json.JSONArray

class AppUsageViewModel : ViewModel() {

    private var mAppUsagesList: MutableLiveData<List<AppUsage>> = MutableLiveData()

    private interface OnAppUsageUpdatedListener {
        fun onAppUsageListUpdated(mAppUsages: List<AppUsage>)
    }

    fun init(context: Context, appUsageDescriptionString: String) {
        UpdateAppUsageListTask(context, object : OnAppUsageUpdatedListener {
            override fun onAppUsageListUpdated(mAppUsages: List<AppUsage>) {
                mAppUsagesList.postValue(mAppUsages)
            }
        }).execute(appUsageDescriptionString)
    }

    fun getAppUsages(): LiveData<List<AppUsage>> {
        return mAppUsagesList
    }

    private class UpdateAppUsageListTask(val context: Context,
                                         val listener: OnAppUsageUpdatedListener): AsyncTask<String, Void?, List<AppUsage>>() {
        override fun doInBackground(vararg params: String?): List<AppUsage> {
            val result = ArrayList<AppUsage>()
            if (params[0] != null) {
                val appUsagesJsonArray = JSONArray(params[0])
                for (i in 0 until appUsagesJsonArray.length()) {
                    val currentAppPackageName = appUsagesJsonArray.getJSONObject(i).getString("package_name")
                    val currentAppUsage = appUsagesJsonArray.getJSONObject(i).getLong("package_usage")
                    val currentAppIcon = context.applicationContext.packageManager.getApplicationIcon(currentAppPackageName)
                    val currentAppInfo = context.applicationContext.packageManager.getApplicationInfo(currentAppPackageName, 0)
                    var currentAppName: String
                    if (currentAppInfo != null) {
                        currentAppName = context.applicationContext.packageManager.getApplicationLabel(currentAppInfo).toString()
                        if (currentAppName.contains('.')) {
                            currentAppName = currentAppPackageName.split('.').last()
                        }
                        result.add(AppUsage(currentAppName, currentAppUsage, currentAppIcon))
                    } else {
                        currentAppName = currentAppPackageName.split('.').last()
                        result.add(AppUsage(currentAppName, currentAppUsage, currentAppIcon))
                    }
                }

                result.sortBy { it.appUsage }
                result.reverse()
            }

            return result
        }

        override fun onPostExecute(result: List<AppUsage>) {
            listener.onAppUsageListUpdated(result)
        }
    }
}