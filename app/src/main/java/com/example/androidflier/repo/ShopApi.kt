package com.example.androidflier.repo

import com.example.androidflier.model.Shop
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopApi {
    /**
     * returns all shops with limit quantity and start from
     */
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "./api/shop/all")
    fun getAllShop(
        @Query("from") from: String = "0",
        @Query("quantity") quantity: String = "10"
    ): Call<List<Shop>>

/*    *//**
     * returns all NEAREST shops with limit quantity and start from
     * api: ".api/shop?lng={lng}&lat={lat}&from=0&quantity=10"
     *//*
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "./api/shop")
    fun getAllNearestShop(
        @Query("lng") lng: Double,
        @Query("lat") lat: Double,
        @Query("from") from: String = "0",
        @Query("quantity") quantity: String = "10"
    ): Call<List<Shop>>*/

    /**
     * returns shop with his stocks
     * api: "./api/shop/{shop id}"
     */
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "/api/shop/{shopId}")
    fun getShopWithStocks(@Path("shopId") shopId: Long): Call<Shop>

    /**
     * returns shops are searched by query text
     * api: "./api/shop/search"
     */
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "/api/shop/search")
    fun search(@Query("text") query: String): Call<List<Shop>>

    /**
     * returns shops are searched by query text
     * api: "./api/shop/search"
     */
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "./api/shop/search")
    fun getAllShopWithSearching(
        @Query("tab") tab: String,
        @Query("searchText") searchText: String,
        @Query("from") from: String = "0",
        @Query("quantity") quantity: String = "10"
    ): Call<List<Shop>>

    /**
     * returns all NEAREST shops with limit quantity and start from and search parameters
     * api: "./api/shop/nearest/search?lng={lng}&lat={lat}&tab={tab}&searchText={searchText}&from={from}&quantity={quantity}"
     */
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "./api/shop/nearest/search")
    fun getAllNearestShopWithSearching(
        @Query("lng") lng: Double,
        @Query("lat") lat: Double,
        @Query("tab") tabSearchText: String,
        @Query("searchText") searchText: String,
        @Query("from") from: String = "0",
        @Query("quantity") quantity: String = "10"
    ): Call<List<Shop>>
}