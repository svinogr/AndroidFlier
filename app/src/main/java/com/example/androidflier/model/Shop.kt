package com.example.androidflier.model

data class Shop(
    var id: Long,
    var created: String,
    var updated: String,
    var status: String,
    var userId: Long,
    var coordLat: Double,
    var coordLng: Double,
    var title: String,
    var address: String,
    var description: String,
    var url: String,
    var img: String,
    var stocks: MutableList<Stock>
)