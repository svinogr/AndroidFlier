package com.example.androidflier.repo

import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.repo.retrofit.RetrofitInst
import retrofit2.Call

class TabRepo : TabReposable {
    private lateinit var tabApi: TabApi

    companion object {
        private lateinit var instance: TabRepo

        init {
            instance = TabRepo()
            instance.tabApi = RetrofitInst.getInstance().tabApi
        }

        fun getInstance(): TabRepo {
            return instance ?: TabRepo()
        }
    }

    override fun getAllTab(): Call<List<Tab>> {
       return tabApi.getAllTab()
    }
}