package com.example.androidflier.repo

import com.example.androidflier.model.Shop
import retrofit2.Call
import retrofit2.http.GET

interface ShopApi {
    @GET
    fun getAllShop(): Call<List<Shop>>
}