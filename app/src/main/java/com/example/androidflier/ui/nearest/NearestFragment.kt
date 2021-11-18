package com.example.androidflier.ui.nearest

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
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
import com.example.androidflier.databinding.FragmentDashboardBinding
import com.example.androidflier.databinding.FragmentNearestBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.localdb.DataBaseHelper
import com.example.androidflier.repo.localdb.ManagerLocalStorage
import com.example.androidflier.ui.viewmodels.ListModelFactory

class NearestFragment : Fragment(R.layout.fragment_nearest), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentNearestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopsViewModel: NearestListShopsViewModel
    private lateinit var adapter: ShopCardAdapter
    private lateinit var shopObserver: Observer<List<Shop>>
    private lateinit var messageObserver: Observer<String>
    private lateinit var refreshLayout: SwipeRefreshLayout

    companion object {
        const val TAG = "NearestFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestBinding.inflate(inflater, container, false)

        shopsViewModel = ViewModelProvider(
            requireActivity(),
            ListModelFactory(requireActivity().application)
        ).get(
            "1",
            NearestListShopsViewModel::class.java
        )

        setRecyclerView()
        setRefreshLayout()
        setObservers()

        refreshLayout.post {
            refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
            onRefresh()
        }

        return binding.root
    }

    private fun setRecyclerView() {
        recyclerView = binding.nearestRecyclerView
        val lManager = LinearLayoutManager(view?.context)
        recyclerView.layoutManager = lManager

        adapter = ShopCardAdapter()
        recyclerView.adapter = adapter
    }

    private fun setObservers() {
        shopObserver = Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                adapter.listShops = it
                adapter.notifyDataSetChanged()
                recyclerView.scheduleLayoutAnimation()
                refreshLayout.isRefreshing = false // без этого не закроется прогрес бар
            }
        }

        shopsViewModel.shops.observe(
            viewLifecycleOwner, shopObserver
        )

        messageObserver = Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                refreshLayout.isRefreshing = false // без этого не закроется прогрес бар
            }
        }

        shopsViewModel.message.observe(viewLifecycleOwner, messageObserver)
    }

    private fun setRefreshLayout() {
        refreshLayout = binding.nearestRefreshLayout
        refreshLayout.setOnRefreshListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shopsViewModel.shops.removeObserver(shopObserver)
        shopsViewModel.message.removeObserver(messageObserver)
        _binding = null
    }

    override fun onRefresh() {
        Log.d("ref", "onRefresh")
        shopsViewModel.refreshData()
    }
}