package com.example.androidflier.adapter.holder

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.androidflier.R
import com.example.androidflier.adapter.StockCardAdapter
import com.example.androidflier.adapter.TabCardAdapter
import com.example.androidflier.model.Stock
import com.example.androidflier.repo.retrofit.RetrofitInst
import com.example.androidflier.ui.shopdetail.ShopDetailFragment

class StockViewHolder(itemView: View, val adapter: StockCardAdapter) :
    RecyclerView.ViewHolder(itemView) {
    private var title: TextView = itemView.findViewById(R.id.stock_layout_title)
    private var img: ImageView = itemView.findViewById(R.id.stock_layout_img)
    private var curPrice: TextView = itemView.findViewById(R.id.stock_layout_price_cur_price)
    private var oldPrice: TextView = itemView.findViewById(R.id.stock_layout_price_old_price)

    fun bind(stock: Stock) {
        title.text = stock.title
        curPrice.text = stock.curPrice.toString()
        oldPrice.text = stock.oldPrice.toString()

        Glide.with(itemView)
            .load(RetrofitInst.IMG_STOCK_URL + stock.img)
            .centerCrop()
            .placeholder(R.drawable.ic_spiner).into(img)

        itemView.setOnClickListener() {
            adapter.stockSelectable(stock)
        }
    }

    interface SheetBottomClickListener {
        fun stockSelectable(stock: Stock)
    }
}