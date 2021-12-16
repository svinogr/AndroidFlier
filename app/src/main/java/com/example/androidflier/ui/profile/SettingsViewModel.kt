package com.example.androidflier.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.model.SettingsSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SettingsViewModel(context: Context) : ViewModel() {
    private var sharedPreferences: SharedPreferences
    private var _settings = MutableLiveData<SettingsSearch>()
    val settings: LiveData<SettingsSearch> = _settings
    private lateinit var settingsSearch: SettingsSearch

    companion object {
        private const val ON = "on"
        private const val RADIUS = "radius"
        private const val TIME = "time"
        private const val TAGS_EDIT = "tags"
        private const val TAG = "SettingsViewModel"
    }

    init {
        val flierApp = context as FlierApp
        sharedPreferences = flierApp.sharedPreferences

        val on = sharedPreferences.getBoolean(ON, false)
        val radius = sharedPreferences.getInt(RADIUS, 1)
        val time = sharedPreferences.getInt(TIME, 4)
        val listTag = getListTagFromSharedPref(sharedPreferences)
        Log.d(TAG, listTag.toString() + "load" + on + radius + time)
        settingsSearch = SettingsSearch(on, time, radius, listTag)

        _settings.value = settingsSearch
    }

    private fun getListTagFromSharedPref(sharedPreferences: SharedPreferences): MutableList<String> {
        val strJson = sharedPreferences.getString(TAGS_EDIT, "")

        return strJson!!.split(" ") as MutableList<String>
    }

    fun getSettings() {

    }

    fun saveSettings(settings: SettingsSearch) {
        GlobalScope.launch(Dispatchers.IO) {
            val edit = sharedPreferences.edit()
            edit.putBoolean(ON, settings.on)
            edit.putInt(RADIUS, settings.radius)
            edit.putInt(TIME, settings.timePeriod)
            edit.putString(TAGS_EDIT, getStringFromList(settings.listTag))
            edit.apply()
Log.d(TAG, settings.listTag.toString())
            _settings.postValue(settings)
        }
    }

    private fun getStringFromList(list: List<String>): String {
        Log.d(TAG, list.toString())
        val str = ""
        list.forEach { s: String -> str.plus(s + " ") }
        Log.d(TAG, str)
        return str.trim()
    }
}