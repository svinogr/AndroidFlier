package com.example.androidflier.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.adapter.holder.ShopViewHolder
import com.example.androidflier.model.Shop

class ShopCardAdapter(var listShops: List<Shop> = mutableListOf()) : RecyclerView.Adapter<ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.shop_layout, parent, false)
        return ShopViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(listShops[position])
    }

    override fun getItemCount(): Int {
        return listShops.size
    }
}