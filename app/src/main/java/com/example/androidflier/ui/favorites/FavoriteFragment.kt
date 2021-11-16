package com.example.androidflier.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidflier.R
import com.example.androidflier.adapter.ShopCardAdapter
import com.example.androidflier.databinding.FragmentFavoriteBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.ListModelFactory

class FavoriteFragment : Fragment(R.layout.fragment_favorite),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var shops: FavoriteViewModel
    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopCardAdapter
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shops =
            ViewModelProvider(
                requireActivity(),
                ListModelFactory(requireActivity().application)
            ).get(FavoriteViewModel::class.java)

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        setRecyclerView()
        setRefreshLayout()

        return binding.root
    }

    private fun setRefreshLayout() {
        refreshLayout = binding.favoriteRefreshLayout
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.post{
            refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
            onRefresh()
        }
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    private fun setRecyclerView() {
        adapter = ShopCardAdapter()

        val lManager = LinearLayoutManager(view?.context)
        recyclerView = binding.favoriteRecyclerView
        recyclerView.layoutManager = lManager
        recyclerView.adapter = adapter

        shops.shops.observe(viewLifecycleOwner, object : Observer<List<Shop>> {
            override fun onChanged(list: List<Shop>) {
                adapter.listShops = list
                adapter.notifyDataSetChanged()

                refreshLayout.isRefreshing = false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRefresh() {
        shops.refreshData()
    }
}