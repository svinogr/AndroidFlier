package com.example.androidflier.model

data class SettingsSearch(
    var on: Boolean,
    var timePeriod: Int,
    var radius: Int,
    var listTag: List<String>) {
}