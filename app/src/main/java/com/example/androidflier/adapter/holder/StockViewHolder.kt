package com.example.androidflier.adapter.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.model.Stock

class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private  var title: TextView
    private  var img: ImageView

    init {
        title = itemView.findViewById(R.id.stock_layout_title)
        img = itemView.findViewById(R.id.stock_layout_img)
    }

    fun bind(stock: Stock) {
        title.text = stock.title
        // todo img
    }
}