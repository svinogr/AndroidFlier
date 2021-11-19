package com.example.androidflier.ui.allshops

import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidflier.R
import com.example.androidflier.adapter.ShopCardAdapter
import com.example.androidflier.adapter.TabCardAdapter
import com.example.androidflier.databinding.FragmentDashboardBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.repo.localdb.ManagerLocalStorage
import com.example.androidflier.ui.viewmodels.ListModelFactory
import okhttp3.internal.notify


class DashboardFragment : Fragment(R.layout.fragment_dashboard),
    SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewTab: RecyclerView
    private lateinit var shopsViewModel: DashboardViewModel
    private lateinit var adapter: ShopCardAdapter
    private lateinit var adapterTab: TabCardAdapter
    private lateinit var shopObserver: Observer<List<Shop>>
    private lateinit var messageObserver: Observer<String>
    private lateinit var tabsObserver: Observer<List<Tab>>
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
        setObservers()
        setRecyclerView()



        return binding.root
    }

    private fun setObservers() {
        shopObserver = Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                adapter.listShops = it
                adapter.notifyDataSetChanged()
                recyclerView.scheduleLayoutAnimation()
                Log.d("ref", "setRecyclerView")
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

        ////testsection
        tabsObserver = Observer {
            Log.d("DashboardViewModel state",  viewLifecycleOwner.lifecycle.currentState.toString())
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Log.d("TAg", it.toString())
                adapterTab.listTab = it
                adapterTab.notifyDataSetChanged()
                recyclerViewTab.scheduleLayoutAnimation()
            }
        }

        shopsViewModel.tab.observe(viewLifecycleOwner, tabsObserver)
    }

    override fun onResume() {
        super.onResume()
        Log.d("DashboardViewModel state on",  viewLifecycleOwner.lifecycle.currentState.toString())

        refreshLayout.post {
            refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
            onRefresh()
        }

    }

    private fun setRefreshLayout() {
        refreshLayout = binding.dashboardRefreshLayout
        refreshLayout.setOnRefreshListener(this)
    }

    private fun setRecyclerView() {
        recyclerView = binding.dashboardRecyclerView
        val lManager = LinearLayoutManager(view?.context)
        recyclerView.layoutManager = lManager

        adapter = ShopCardAdapter()
        recyclerView.adapter = adapter

        recyclerViewTab = binding.dashboardRecyclerTabView
        val TLManager = LinearLayoutManager(view?.context)
        TLManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewTab.layoutManager = TLManager

        adapterTab = TabCardAdapter()
        recyclerViewTab.adapter = adapterTab
    }

    override fun onRefresh() {
        Log.d("ref", "onRefresh")
        shopsViewModel.refreshData()
        shopsViewModel.getTab()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        shopsViewModel.shops.removeObserver(shopObserver)
        shopsViewModel.message.removeObserver(messageObserver)
        shopsViewModel.tab.removeObserver(tabsObserver)
        _binding = null
    }
}
