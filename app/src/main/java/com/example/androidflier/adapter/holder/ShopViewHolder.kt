package com.example.androidflier.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.model.Shop

class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView

    init {
        title = itemView.findViewById(R.id.shop_cardView_titleText)
    }

    fun bind(shop: Shop) {
        title.text = shop.title
    }
}