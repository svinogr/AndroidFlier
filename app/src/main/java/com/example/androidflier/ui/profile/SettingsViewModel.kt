package com.example.androidflier.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.androidflier.FlierApp
import com.example.androidflier.model.SettingsSearch
import com.example.androidflier.model.SettingsSearch.Companion.RADIUS
import com.example.androidflier.model.SettingsSearch.Companion.TAGS_EDIT
import com.example.androidflier.model.SettingsSearch.Companion.TIME
import com.example.androidflier.model.SettingsSearch.Companion.WORKER_WORK
import com.example.androidflier.util.ShopWorker
import com.example.androidflier.util.ShopWorker.Companion.SHOP_WORKER


class SettingsViewModel(private val context: Context) : ViewModel() {
    private lateinit var sharedPreferences: SharedPreferences
    private var _settings = MutableLiveData<SettingsSearch>()
    val settings: LiveData<SettingsSearch> = _settings
    private lateinit var settingsSearch: SettingsSearch
    private val TAG = "SettingsViewModel"

    fun getSettings() {
        val flierApp = context as FlierApp
        sharedPreferences = flierApp.sharedPreferences

        val on = sharedPreferences.getBoolean(WORKER_WORK, false)
        val radius = sharedPreferences.getInt(RADIUS, 1)
        val time = sharedPreferences.getInt(TIME, 4)
        val listTag = SettingsSearch.getListTagFromSharedPref(sharedPreferences)
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
        edit.putString(TAGS_EDIT, SettingsSearch.getStringFromList(settings.listTag))
        edit.apply()
        //}
    }

    fun stopOrStartShopWorker(toOn: Boolean) {
        val isWorkerWork = sharedPreferences.getBoolean(SHOP_WORKER, false)
        Log.d("stat worker", isWorkerWork.toString())
        Log.d("stat worker", toOn.toString())

        if (isWorkerWork && toOn) return // при первом запуске воркера здесь фолс

        if (!toOn) {
            WorkManager.getInstance(context).cancelUniqueWork(SHOP_WORKER)
        } else {
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
        edit.putBoolean(SHOP_WORKER, isWork)
        edit.apply()
    }
}