package com.example.androidflier.repo

import com.example.androidflier.model.Tab
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface TabApi {
    @Headers(
        "Accept: application/json",
        "Content-type:application/json"
    )
    @GET(
        value = "./api/tab"
    )
    fun getAllTab(): Call<List<Tab>>

}