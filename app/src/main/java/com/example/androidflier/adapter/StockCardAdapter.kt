package com.example.androidflier.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.adapter.holder.StockViewHolder
import com.example.androidflier.model.Stock

class StockCardAdapter(
    var stocks: List<Stock> = mutableListOf(),
) : RecyclerView.Adapter<StockViewHolder>() {
    lateinit var stockListener: StockViewHolder.SheetBottomClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.stock_layout, parent, false)
        return StockViewHolder(inflate, this)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    fun stockSelectable(stock: Stock) {
        Log.d("stoc", stock.id.toString())
        stockListener.stockSelectable(stock)
    }
}