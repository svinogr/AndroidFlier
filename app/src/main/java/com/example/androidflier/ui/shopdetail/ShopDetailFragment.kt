package com.example.androidflier.ui.shopdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidflier.R
import com.example.androidflier.adapter.StockCardAdapter
import com.example.androidflier.databinding.FragmentDashboardBinding
import com.example.androidflier.databinding.FragmentShopDetailBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.SingleEntityModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ShopDetailFragment : Fragment(R.layout.fragment_shop_detail),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recycler: RecyclerView
    private var _binding: FragmentShopDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var shop: ShopViewModel
    private lateinit var adapter: StockCardAdapter
    private lateinit var bottomSheetLayout: LinearLayout
    private lateinit var btnFavorite: ImageButton
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var messageObserver: Observer<String>
    private lateinit var shopObserver: Observer<Shop>

    companion object {
        const val ARG_ID_LONG = "id"
        const val TAG = "ShopDetailFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShopDetailBinding.inflate(inflater, container, false)

        val id = requireArguments().getLong(ARG_ID_LONG)

        shop = ViewModelProvider(
            this,
            SingleEntityModelFactory(id, requireActivity().application)
        ).get(TAG, ShopViewModel::class.java)

        bottomSheetLayout = binding.bottomSheet.bottomSheet

        setBottomSheetBehavior()
        setFavoriteBtn()
        setRecyclerView()
        setRefreshLayout()
        setObservers()

        refreshLayout.post {
            refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
            onRefresh()
        }

        return binding.root
    }

    private fun setObservers() {
        messageObserver = Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        shop.message.observe(viewLifecycleOwner, messageObserver)

        shopObserver = Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {

                if (it.id == 0L) {
                    Toast.makeText(requireContext(), "такого магазина нет", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().supportFragmentManager.popBackStack() // не знаю правильно так или нет
                    refreshLayout.isRefreshing = false
                } else {
                    binding.shopDetailFragmentTitleShop.text = it.title
                    bottomSheetLayout.findViewById<TextView>(R.id.bottom_sheet_title).text =
                        it.title + it.favoriteStatus.toString()
                    bottomSheetLayout.findViewById<TextView>(R.id.bottom_sheet_body).text =
                        it.description

                    if (it.favoriteStatus) btnFavorite.setImageResource(R.drawable.ic_heart_red) else btnFavorite.setImageResource(
                        R.drawable.ic_heart_white
                    )

                    adapter.stocks = it.stocks
                    adapter.notifyDataSetChanged()
                    recycler.scheduleLayoutAnimation()

                    refreshLayout.isRefreshing = false
                }
            }
        }

        shop.shop.observe(viewLifecycleOwner, shopObserver)
    }

    private fun setRefreshLayout() {
        refreshLayout = binding.shopDetailRefreshLayout
        refreshLayout.setOnRefreshListener(this)
    }

    private fun setRecyclerView() {
        val layout = GridLayoutManager(requireContext(), 2)
        recycler = binding.shopDetailFragmentRecyclerStocks
        recycler.layoutManager = layout
        adapter = StockCardAdapter()
        recycler.adapter = adapter
    }

    private fun setFavoriteBtn() {
        btnFavorite = binding.bottomSheet.saveToFavorite

        btnFavorite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                shop.changeFavoriteStatus()
            }
        })
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

    override fun onRefresh() {
        shop.refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        shop.shop.removeObserver(shopObserver)
        shop.message.removeObserver(messageObserver)
        _binding = null
    }
}