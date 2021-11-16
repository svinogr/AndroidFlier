package com.example.androidflier.ui.allshops

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidflier.R
import com.example.androidflier.adapter.ShopCardAdapter
import com.example.androidflier.databinding.FragmentDashboardBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.localdb.ManagerLocalStorage
import com.example.androidflier.ui.viewmodels.ListModelFactory


class DashboardFragment : Fragment(R.layout.fragment_dashboard),
    SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopsViewModel: DashboardViewModel
    private lateinit var adapter: ShopCardAdapter
    private lateinit var shopObserver: Observer<List<Shop>>
    private lateinit var progress: ProgressBar
    private lateinit var refreshLayout: SwipeRefreshLayout


    companion object {
        const val TAG = "DashboardFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        shopsViewModel = ViewModelProvider(
            requireActivity(),
            ListModelFactory(requireActivity().application)
        ).get(TAG, DashboardViewModel::class.java)

        setRefreshLayout()
        setRecyclerView()


        return binding.root
    }

    private fun setRefreshLayout() {
        refreshLayout = binding.dashboardRefreshLayout
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.post{
            refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
            onRefresh()
        }
    }

    private fun setRecyclerView() {
        recyclerView = binding.dashboardRecyclerView
        val lManager = LinearLayoutManager(view?.context)
        recyclerView.layoutManager = lManager

        adapter = ShopCardAdapter()
        recyclerView.adapter = adapter

        shopObserver = Observer {
            adapter.listShops = it
            adapter.notifyDataSetChanged()

            refreshLayout.isRefreshing = false // без этого не закроется прогрес бар
        }

        shopsViewModel.shops.observe(
            viewLifecycleOwner, shopObserver
        )

        onRefresh()
    }

    override fun onRefresh() {
        shopsViewModel.refreshData()
    }
}