package com.example.androidflier.repo

import com.example.androidflier.model.Shop
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopApi {
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "./api/shop/all")
    fun getAllShop( @Query("from") from : String = "0",
                    @Query("quantity") quantity : String = "10"): Call<List<Shop>>

    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
  //  @GET(value = ".api/shop?lng={lng}&lat={lat}&from=0&quantity=10")
    @GET(value = "./api/shop")
    fun getAllNearestShop(@Query("lng") lng: Double,
                          @Query("lat") lat: Double,
                          @Query("from") from : String = "0",
                          @Query("quantity") quantity : String = "10"): Call<List<Shop>>
}