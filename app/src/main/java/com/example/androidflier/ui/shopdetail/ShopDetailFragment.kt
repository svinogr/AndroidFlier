package com.example.androidflier.ui.shopdetail

import android.os.Bundle
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
import com.example.androidflier.adapter.holder.StockViewHolder
import com.example.androidflier.databinding.FragmentShopDetailBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Stock
import com.example.androidflier.ui.viewmodels.SingleEntityModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ShopDetailFragment : Fragment(R.layout.fragment_shop_detail),
    SwipeRefreshLayout.OnRefreshListener, StockViewHolder.SheetBottomClickListener {

    private lateinit var bottomSheetBehaviorShop: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorStock: BottomSheetBehavior<LinearLayout>
    private lateinit var recycler: RecyclerView
    private var _binding: FragmentShopDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var shop: ShopViewModel
    private lateinit var adapter: StockCardAdapter
    private lateinit var bottomSheetLayoutShop: LinearLayout
    private lateinit var bottomSheetLayoutStock: LinearLayout
    private lateinit var btnFavorite: ImageButton
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var messageObserver: Observer<String>
    private lateinit var shopObserver: Observer<Shop>
    private var selectStock: Stock? = null

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

        bottomSheetLayoutShop = binding.bottomSheetShop.bottomSheet
        bottomSheetLayoutStock = binding.bottomSheetStock.bottomSheetStock

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

                    bottomSheetLayoutShop.findViewById<TextView>(R.id.bottom_sheet_title).text =
                        it.title

                    bottomSheetLayoutShop.findViewById<TextView>(R.id.bottom_sheet_description).text =
                        it.description

                    bottomSheetLayoutShop.findViewById<TextView>(R.id.bottom_sheet_address).text =
                        it.address

                    bottomSheetLayoutShop.findViewById<TextView>(R.id.bottom_sheet_phone).text =
                        it.phone

                    //TODO для плавности возможно стоит сделать появление сердечка из режима невидимости

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
        adapter.stockListener = this
        recycler.adapter = adapter
    }

    private fun setFavoriteBtn() {
        btnFavorite = binding.saveToFavorite

        btnFavorite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                shop.changeFavoriteStatus()
            }
        })
    }

    private fun setBottomSheetBehavior() {
        bottomSheetBehaviorShop = BottomSheetBehavior.from(binding.bottomSheetShop.bottomSheet)

        bottomSheetBehaviorShop.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehaviorShop.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehaviorShop.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehaviorShop.peekHeight = 340
        bottomSheetBehaviorShop.isHideable = true

        binding.shopDetailFragmentBtnInfo.setOnClickListener() {
            if(bottomSheetBehaviorStock.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehaviorStock.state = BottomSheetBehavior.STATE_HIDDEN
            }

            if (bottomSheetBehaviorShop.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehaviorShop.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomSheetBehaviorShop.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        bottomSheetBehaviorStock = BottomSheetBehavior.from(binding.bottomSheetStock.bottomSheetStock)

        bottomSheetBehaviorStock.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehaviorStock.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehaviorStock.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehaviorStock.peekHeight = 340
        bottomSheetBehaviorStock.isHideable = true
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

    override fun stockSelectable(stock: Stock) {
        if(selectStock == null) selectStock = stock

        bottomSheetLayoutStock.findViewById<TextView>(R.id.bottom_sheet_title_stock).text =
            stock.id.toString()

        bottomSheetLayoutStock.findViewById<TextView>(R.id.bottom_sheet_description_stock).text =
            stock.description


        if(bottomSheetBehaviorShop.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorShop.state = BottomSheetBehavior.STATE_HIDDEN
        }

        if (bottomSheetBehaviorStock.state == BottomSheetBehavior.STATE_EXPANDED && selectStock!!.id == stock.id) {
            bottomSheetBehaviorStock.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            bottomSheetBehaviorStock.state = BottomSheetBehavior.STATE_EXPANDED
        }

        selectStock = stock
    }
}