package com.example.androidflier.adapter.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidflier.R
import com.example.androidflier.model.Stock
import com.example.androidflier.repo.retrofit.RetrofitInst

class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var title: TextView = itemView.findViewById(R.id.stock_layout_title)
    private var img: ImageView = itemView.findViewById(R.id.stock_layout_img)
    private var curPrice: TextView = itemView.findViewById(R.id.stock_layout_price_cur_price)
    private var oldPrice: TextView = itemView.findViewById(R.id.stock_layout_price_old_price)

    fun bind(stock: Stock) {
        title.text = stock.title
        curPrice.text = stock.curPrice.toString()
        oldPrice.text = stock.oldPrice.toString()

        Glide.with(itemView).load(RetrofitInst.BASE_URL).centerCrop().placeholder(R.drawable.ic_spiner).into(img)
    }
}