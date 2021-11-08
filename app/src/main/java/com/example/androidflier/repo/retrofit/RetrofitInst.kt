package com.example.androidflier.repo.retrofit

import com.example.androidflier.repo.ShopApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://192.168.43.214:8081/"
class RetrofitInst {
    lateinit var shopApi: ShopApi

    companion object {
        private lateinit var instance: RetrofitInst

        init {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            instance = RetrofitInst()
            instance.shopApi = retrofit.create(ShopApi::class.java)
        }

        fun getInstance(): RetrofitInst {
            return instance ?: RetrofitInst()
        }
    }
}