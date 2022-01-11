package com.example.androidflier.repo

import com.example.androidflier.model.Stock
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface StockApi {
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(value = "./api/{shopId}/stock") // http://localhost:8081/api/shop/1/stock?from=0&quantity=10
    fun getStocksByShopId(@Path("shopId") shopId: Long,
                          @Query("from") from : String = "0",
                          @Query("quantity") quantity : String = "10"): Call<List<Stock>>
}