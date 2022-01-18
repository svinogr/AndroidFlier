package com.example.androidflier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.adapter.holder.LoadingViewHolder
import com.example.androidflier.adapter.holder.ShopViewHolder
import com.example.androidflier.model.Shop

const val VIEW_TYPE_VIEW = 0
const val VIEW_TYPE_LOADING = 1

class ShopCardAdapter(var listShops: List<Shop> = mutableListOf()) :
    RecyclerView.Adapter<ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ShopViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.shop_layout, parent, false)
        return ShopViewHolder(inflate)
    /*    when (viewType) {
            VIEW_TYPE_VIEW -> {
                val inflate =
                    LayoutInflater.from(parent.context).inflate(R.layout.shop_layout, parent, false)
                return ShopViewHolder(inflate)
            }
            else -> {
                val inflate =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.loading_layout, parent, false)
                return LoadingViewHolder(inflate)
            }
        }*/
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
      /*  if (holder is ShopViewHolder) {
            holder.bind(listShops[position])
        }*/
        holder.bind(listShops[position])
    }

    override fun getItemCount(): Int {
        return listShops.size
    }

 /*   override fun getItemViewType(position: Int): Int {
        return if (listShops[position].id != - 1L ) {
            VIEW_TYPE_VIEW
        }else {
            VIEW_TYPE_LOADING
        }
    }*/
}