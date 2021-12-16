package com.example.androidflier.model

data class SettingsSearch(
    var on: Boolean = true ,
    var timePeriod: Int = 0,
    var radius: Int = 4,
    var listTag: MutableList<String> = mutableListOf()
) {
}