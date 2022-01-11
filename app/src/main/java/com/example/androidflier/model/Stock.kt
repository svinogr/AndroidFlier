package com.example.androidflier.model

data class Stock(val id: Long,
                 val created: String,
                 val updated: String,
                 val status: String,
                 val shopId: Long,
                 val title: String,
                 val description: String,
                 val dateStart: String,
                 val dateFinish: String,
                 val img: String,
                 val url: String,
                 val oldPrice: Double,
                 val curPrice: Double)