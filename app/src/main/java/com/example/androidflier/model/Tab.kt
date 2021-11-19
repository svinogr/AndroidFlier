package com.example.androidflier.model

data class Tab(
    val id: Int,
    val title: String,
    var selected: Boolean = false
)