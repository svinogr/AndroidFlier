package com.example.androidflier.adapter.holder

import android.view.RoundedCorner
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.androidflier.R
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.retrofit.RetrofitInst
import com.example.androidflier.ui.shopdetail.ShopDetailFragment

class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var title: TextView = itemView.findViewById(R.id.shop_cardView_title)
    private var address: TextView = itemView.findViewById(R.id.shop_cardView_address)
    private var img: ImageView = itemView.findViewById(R.id.shop_cardView_imageView)

    fun bind(shop: Shop) {
        title.text = shop.title + shop.id
        address.text = shop.address

        val transformation = MultiTransformation(CenterCrop(), RoundedCorners(16))
        Glide.with(itemView)
            .load(RetrofitInst.IMG_SHOP_URL + shop.img)
            .transform(transformation)
            .placeholder(R.drawable.ic_spiner).into(img)

        itemView.setOnClickListener() {
            it.findNavController().navigate(
                R.id.shopDetailFragment, bundleOf(
                    ShopDetailFragment.ARG_ID_LONG to shop.id
                )
            )
        }
    }
}
