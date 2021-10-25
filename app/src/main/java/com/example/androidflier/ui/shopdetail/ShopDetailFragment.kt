package com.example.androidflier.ui.shopdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.adapter.StockCardAdapter
import com.example.androidflier.databinding.FragmentNearestBinding
import com.example.androidflier.databinding.FragmentShopDetailBinding
import com.example.androidflier.model.Stock
import com.example.androidflier.ui.viewmodels.SingleEntityModelFactory

class ShopDetailFragment : Fragment(R.layout.fragment_shop_detail) {
    companion object{
        const val ARG_ID_LONG = "id"
    }

    private lateinit var recycler: RecyclerView
    private lateinit var binding: FragmentShopDetailBinding
    private lateinit var shop: ShopViewModel
    private lateinit var adapter: StockCardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentShopDetailBinding.bind(view)

        val id = requireArguments().getLong(ARG_ID_LONG)
        shop = ViewModelProvider(requireActivity(), SingleEntityModelFactory(id)).get(ShopViewModel::class.java)
   /*     shop.shop.observe(viewLifecycleOwner){
            binding.shopDetailFragmentTitleShop.text = it.title
        }*/

        recycler = binding.shopDetailFragmentRecyclerStocks
        val layout = GridLayoutManager(view.context, 2)
        recycler.layoutManager = layout

        adapter = StockCardAdapter()

        shop.shop.observe(viewLifecycleOwner){
            adapter.stocks = it.listStock
            adapter.notifyDataSetChanged()
        }
    }


}