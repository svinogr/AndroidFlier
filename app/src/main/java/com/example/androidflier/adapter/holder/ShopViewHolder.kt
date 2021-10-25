package com.example.androidflier.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.shopdetail.ShopDetailFragment

class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var title: TextView
    private var address: TextView

    init {
        title = itemView.findViewById(R.id.shop_cardView_title)
        address = itemView.findViewById(R.id.shop_cardView_address)
    }

    fun bind(shop: Shop) {
        title.text = shop.title
        address.text = shop.address
        itemView.setOnClickListener(){
            it.findNavController().navigate(R.id.action_navigation_home_to_shopDetailFragment, bundleOf(
                ShopDetailFragment.ARG_ID_LONG to shop.id))
        }
    }
}