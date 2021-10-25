package com.example.androidflier.repo

import com.example.androidflier.model.Shop
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitApi {
    @GET("api/shop/")
    fun getAllShops() : Call<List<Shop>>

    companion object {

        var BASE_URL = "localhost/"

        fun create() : RetrofitApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(RetrofitApi::class.java)

        }
    }
}