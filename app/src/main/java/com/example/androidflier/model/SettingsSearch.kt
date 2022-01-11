package com.example.androidflier.model

import android.content.SharedPreferences
import java.lang.StringBuilder

data class SettingsSearch(
    var on: Boolean = true ,
    var timePeriod: Int = 0,
    var radius: Int = 4,
    var listTag: List<String> = listOf()
) {
    companion object {
        const val WORKER_WORK = "on"
         const val RADIUS = "radius"
         const val TIME = "time"
         const val TAGS_EDIT = "tags"
         const val TAG = "SettingsViewModel"

         fun getStringFromList(list: List<String>): String {
            var str = StringBuilder()

            for (s in list) {
                str = str.append(s + " ")
            }

            return str.toString().trim()
        }

         fun getListTagFromSharedPref(sharedPreferences: SharedPreferences): MutableList<String> {
            val strJson = sharedPreferences.getString(TAGS_EDIT, "")

            return strJson!!.split(" ") as MutableList<String>
        }

    }
}