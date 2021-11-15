package com.example.androidflier.ui.shopdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.adapter.StockCardAdapter
import com.example.androidflier.databinding.FragmentShopDetailBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.SingleEntityModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ShopDetailFragment : Fragment(R.layout.fragment_shop_detail) {

    private lateinit var recycler: RecyclerView
    private lateinit var binding: FragmentShopDetailBinding
    private lateinit var shop: ShopViewModel
    private lateinit var adapter: StockCardAdapter
    private lateinit var bottomSheetLayout: LinearLayout
    private lateinit var btnFavorite: ImageButton

    companion object {
        const val ARG_ID_LONG = "id"
        const val TAG = "ShopDetailFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentShopDetailBinding.bind(view)

        val id = requireArguments().getLong(ARG_ID_LONG)

        shop = ViewModelProvider(
            this,
            SingleEntityModelFactory(id, requireActivity().application)
        ).get(TAG, ShopViewModel::class.java)

        bottomSheetLayout = binding.bottomSheet.bottomSheet

        setBottomSheetBehavior()

        btnFavorite = binding.bottomSheet.saveToFavorite

        btnFavorite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                shop.changeFavoriteStatus()
            }
        })

        val layout = GridLayoutManager(requireContext(), 2)
        recycler = binding.shopDetailFragmentRecyclerStocks
        recycler.layoutManager = layout
        adapter = StockCardAdapter()
        recycler.adapter = adapter

        shop.shop.observe(viewLifecycleOwner) {
            adapter.stocks = it.stocks
            adapter.notifyDataSetChanged()
            binding.shopDetailFragmentTitleShop.text = it.title
            bottomSheetLayout.findViewById<TextView>(R.id.bottom_sheet_title).text =
                it.title + it.favoriteStatus.toString()
            bottomSheetLayout.findViewById<TextView>(R.id.bottom_sheet_body).text = it.description

            if (it.favoriteStatus) btnFavorite.setImageResource(R.drawable.ic_heart_red) else btnFavorite.setImageResource(R.drawable.ic_heart_white)
        }
    }

    private fun changeStatusFavoriteShop(shop: Shop) {
        if (shop.favoriteStatus) {
            this.shop.deleteFromLocalDb(shop)
            this.shop.shop.value?.favoriteStatus = false

        } else {
            this.shop.saveToLocalDb(shop)
            this.shop.shop.value?.favoriteStatus = true
        }

        Log.d(TAG, shop.favoriteStatus.toString())

    }

    private fun setBottomSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.peekHeight = 340
        bottomSheetBehavior.isHideable = true

        binding.shopDetailFragmentBtnInfo.setOnClickListener() {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}