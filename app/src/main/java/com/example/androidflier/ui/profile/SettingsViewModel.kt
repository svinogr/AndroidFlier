package com.example.androidflier.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.androidflier.FlierApp
import com.example.androidflier.model.SettingsSearch
import com.example.androidflier.util.ShopWorker
import java.lang.StringBuilder


class SettingsViewModel(private val context: Context) : ViewModel() {
    private lateinit var sharedPreferences: SharedPreferences
    private var _settings = MutableLiveData<SettingsSearch>()
    val settings: LiveData<SettingsSearch> = _settings
    private lateinit var settingsSearch: SettingsSearch

    companion object {
        const val WORKER_WORK = "on"
        private const val RADIUS = "radius"
        private const val TIME = "time"
        private const val TAGS_EDIT = "tags"
        private const val TAG = "SettingsViewModel"
    }

    private fun getListTagFromSharedPref(sharedPreferences: SharedPreferences): MutableList<String> {
        val strJson = sharedPreferences.getString(TAGS_EDIT, "")

        return strJson!!.split(" ") as MutableList<String>
    }

    fun getSettings() {
        val flierApp = context as FlierApp
        sharedPreferences = flierApp.sharedPreferences

        val on = sharedPreferences.getBoolean(WORKER_WORK, false)
        val radius = sharedPreferences.getInt(RADIUS, 1)
        val time = sharedPreferences.getInt(TIME, 4)
        val listTag = getListTagFromSharedPref(sharedPreferences)
        Log.d(TAG, listTag.toString() + "load" + on + radius + time)
        settingsSearch = SettingsSearch(on, time, radius, listTag)

        _settings.value = settingsSearch
    }

    fun saveSettings(settings: SettingsSearch) {
      //  GlobalScope.launch(Dispatchers.IO) {
        Log.d(TAG, settings.toString())
            val edit = sharedPreferences.edit()
            edit.putBoolean(WORKER_WORK, settings.on)
            edit.putInt(RADIUS, settings.radius)
            edit.putInt(TIME, settings.timePeriod)
            edit.putString(TAGS_EDIT, getStringFromList(settings.listTag))
            edit.apply()
        //}
    }

    private fun getStringFromList(list: List<String>): String {
        var str = StringBuilder()

        for (s in list) {
            str = str.append(s + " ")
        }

        return str.toString().trim()
    }

    fun stopOrStartShopWorker(toOn: Boolean) {
        val isWorkerWork = sharedPreferences.getBoolean(WORKER_WORK, true)
        Log.d("stat worker", isWorkerWork.toString())

        if (isWorkerWork && toOn) return

        if(!toOn) {
            WorkManager.getInstance(context).cancelUniqueWork(ShopWorker.SHOP_WORKER)
        }else{
            val constraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequest
                .Builder(ShopWorker::class.java)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }

        saveStateWorker(toOn)
    }

    fun saveStateWorker(isWork: Boolean) {
        Log.d("save state work", isWork.toString())
        val edit = sharedPreferences.edit()
        edit.putBoolean(ShopWorker.SHOP_WORKER, isWork)
        edit.apply()
    }
}