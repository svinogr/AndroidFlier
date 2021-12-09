package com.example.androidflier.repo

import com.example.androidflier.model.Tab
import retrofit2.Call

interface TabReposable {
    fun getAllTab(): Call<List<Tab>>
}