package com.example.androidflier.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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
    private lateinit var shopObserver: Observer<List<Shop>>
    private lateinit var messageObserver: Observer<String>

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

        setRefreshLayout()
        setRecyclerView()
        setObservers()

        refreshLayout.post {
            refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
            onRefresh()
        }

        return binding.root
    }

    private fun setObservers() {
        shopObserver = Observer<List<Shop>> {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if(adapter.listShops.isEmpty()) {

                    recyclerView.scheduleLayoutAnimation()
                }

                adapter.listShops = it
                adapter.notifyDataSetChanged()

                refreshLayout.isRefreshing = false
            }
        }

        shops.shops.observe(viewLifecycleOwner, shopObserver)

        messageObserver = Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                refreshLayout.isRefreshing = false
            }
        }

        shops.message.observe(viewLifecycleOwner, messageObserver)
    }

    private fun setRefreshLayout() {
        refreshLayout = binding.favoriteRefreshLayout
        refreshLayout.setOnRefreshListener(this)
    }

    private fun setRecyclerView() {
        adapter = ShopCardAdapter()

        val lManager = LinearLayoutManager(view?.context)
        recyclerView = binding.favoriteRecyclerView
        recyclerView.layoutManager = lManager
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shops.shops.removeObserver(shopObserver)
        shops.message.removeObserver(messageObserver)
        _binding = null
    }

    override fun onRefresh() {
        shops.getData(null, "")
    }
}