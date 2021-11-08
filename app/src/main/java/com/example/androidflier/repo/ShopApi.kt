package com.example.androidflier.repo

import com.example.androidflier.model.Shop
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ShopApi {
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "./api/shop/all?from=0&quantity=10")
    fun getAllShop(): Call<List<Shop>>
}