package com.example.androidflier.repo.retrofit

import com.example.androidflier.repo.ShopApi
import com.example.androidflier.repo.StockApi
import com.example.androidflier.repo.TabApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInst {
    lateinit var shopApi: ShopApi
    lateinit var stockApi: StockApi
    lateinit var tabApi: TabApi

    companion object {
        private lateinit var instance: RetrofitInst
        const val BASE_URL = "http://192.168.43.214:8081/"
        const val IMG_SHOP_URL = BASE_URL + "api/img/shop/"
        const val IMG_STOCK_URL = BASE_URL + "api/img/stock/"

        init {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            instance = RetrofitInst()
            instance.shopApi = retrofit.create(ShopApi::class.java)
            instance.stockApi = retrofit.create(StockApi::class.java)
            instance.tabApi = retrofit.create(TabApi::class.java)
        }

        fun getInstance(): RetrofitInst {
            return instance ?: RetrofitInst()
        }
    }
}