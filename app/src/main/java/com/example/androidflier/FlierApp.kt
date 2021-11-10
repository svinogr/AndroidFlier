package com.example.androidflier

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.example.androidflier.repo.ShopApi
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.localdb.DataBaseHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.*

class FlierApp() : Application() {
   }