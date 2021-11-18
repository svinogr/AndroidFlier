package com.example.androidflier.repo.retrofit

import com.example.androidflier.repo.ShopApi
import com.example.androidflier.repo.StockApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "http://192.168.43.214:8081/"
class RetrofitInst {
    lateinit var shopApi: ShopApi
    lateinit var stockApi: StockApi

    companion object {
        private lateinit var instance: RetrofitInst

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
        }

        fun getInstance(): RetrofitInst {
            return instance ?: RetrofitInst()
        }
    }
}