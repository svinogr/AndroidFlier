package com.example.androidflier.adapter.holder

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidflier.R
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.retrofit.RetrofitInst
import com.example.androidflier.ui.shopdetail.ShopDetailFragment

class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var title: TextView = itemView.findViewById(R.id.shop_cardView_title)
    private var address: TextView = itemView.findViewById(R.id.shop_cardView_address)
    private var img: ImageView = itemView.findViewById(R.id.shop_cardView_imageView)


    fun bind(shop: Shop) {
        title.text = shop.title
        address.text = shop.address

        Glide.with(itemView).load(RetrofitInst.BASE_URL).centerCrop().placeholder(R.drawable.ic_spiner).into(img)

        itemView.setOnClickListener() {
            it.findNavController().navigate(
                R.id.shopDetailFragment, bundleOf(
                    ShopDetailFragment.ARG_ID_LONG to shop.id
                )
            )
        }
    }
}
