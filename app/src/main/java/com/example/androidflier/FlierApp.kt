package com.example.androidflier

import android.app.Application
import com.example.androidflier.repo.ShopApi
import com.example.androidflier.repo.ShopRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class FlierApp() : Application() {
    lateinit var cO: Object
/// подумать как создать переменную со ссылкой на репо
    override fun onCreate() {
        super.onCreate()

    }

    companion object {
        lateinit var retrofit: Retrofit
        lateinit var shopRepository: ShopRepository
        lateinit var instance: FlierApp

        init {
            instance = FlierApp()
            configureRetrofit()
            configureShopRepo()
        }

        fun getInstance(): FlierApp {
            return instance ?: FlierApp()
        }


        private fun configureShopRepo() {
            shopRepository = ShopRepository.getInstance()
            shopRepository.shopApi = retrofit.create(ShopApi::class.java)
        }

        private fun configureRetrofit() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(instance.resources.getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}