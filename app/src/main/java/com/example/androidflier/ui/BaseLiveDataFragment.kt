package com.example.androidflier.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidflier.adapter.ShopCardAdapter
import com.example.androidflier.adapter.TabCardAdapter
import com.example.androidflier.adapter.holder.TabViewHolder
import com.example.androidflier.databinding.FragmentDashboardBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.ui.allshops.DashboardFragment
import com.example.androidflier.ui.viewmodels.BaseShopsViewModel

abstract class BaseLiveDataFragment(fragmentDashboard: Int) : Fragment(fragmentDashboard),
    SwipeRefreshLayout.OnRefreshListener,
    TabViewHolder.TabSelectable {
    private val requestFeatureLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onGotPermissionsResultForFeature
    )
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewTab: RecyclerView
    open lateinit var shopsViewModel: BaseShopsViewModel
    private lateinit var adapter: ShopCardAdapter
    private lateinit var adapterTab: TabCardAdapter
    private lateinit var shopObserver: Observer<List<Shop>>
    private lateinit var messageObserver: Observer<String>
    private lateinit var tabsObserver: Observer<List<Tab>>
    open lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView
    open var selectedTab: Tab? = null
    open var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        setViewModel()
        setRefreshLayout()
        setObservers()
        setRecyclerView()
        setSearchView()

        return binding.root
    }

    abstract fun setViewModel()
    abstract fun isNeedPermissions(): Boolean

    private fun setSearchView() {
        searchView = binding.dashboardSeacrh
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                Log.d(DashboardFragment.TAG, "close searchbar ")
                searchText = ""
                searchView.onActionViewCollapsed()
                // стоит ли ??
                refreshLayout.isRefreshing = true
                onRefresh()

                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchText = query ?: ""
                refreshLayout.isRefreshing = true
                onRefresh()

                // shopsViewModel.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("search", newText.let { newText.toString() })
                return true
            }
        })
    }

    private fun setObservers() {
        shopObserver = Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (adapter.listShops.isEmpty()) {
                    recyclerView.scheduleLayoutAnimation()
                }

                if (!it.isEmpty()) {
                    adapter.listShops = it
                    adapter.notifyDataSetChanged()
                    Log.d("ref", "setRecyclerView")
                    refreshLayout.isRefreshing = false // без этого не закроется прогрес бар
                }
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


        tabsObserver = Observer {
            Log.d("DashboardViewModel state", viewLifecycleOwner.lifecycle.currentState.toString())
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Log.d("TAg", it.toString())
                if (!it.isEmpty()) {
                    adapterTab.listTab = it
                    adapterTab.notifyDataSetChanged()
                    recyclerViewTab.scheduleLayoutAnimation()
                }
            }
        }

        shopsViewModel.tabs.observe(viewLifecycleOwner, tabsObserver)
    }

    override fun onResume() {
        super.onResume()
        Log.d("DashboardViewModel state on", viewLifecycleOwner.lifecycle.currentState.toString())

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
        val tLManager = LinearLayoutManager(view?.context)
        tLManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewTab.layoutManager = tLManager

        adapterTab = TabCardAdapter(tabSelectable = this)
        recyclerViewTab.adapter = adapterTab

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (lManager.findLastCompletelyVisibleItemPosition() == shopsViewModel.shops.value!!.size - 1) {
                    refreshLayout.isRefreshing = true
                    shopsViewModel.loadMore(selectedTab, searchText)
                    refreshLayout.isRefreshing = false
                }
            }
        })
    }

    override fun onRefresh() {
        Log.d("ref", "onRefresh")
        if (isNeedPermissions()) {
            requestFeatureLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            refreshData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        shopsViewModel.shops.removeObserver(shopObserver)
        shopsViewModel.message.removeObserver(messageObserver)
        shopsViewModel.tabs.removeObserver(tabsObserver)
        _binding = null
    }

    override fun withTab(tab: Tab) {
        refreshLayout.isRefreshing = true

        selectedTab = if (tab.selected) tab else null

        //onRefresh()
        if (selectedTab == null) {

            onRefresh()
        } else {
            shopsViewModel.clearData()

            shopsViewModel.refreshDataSearch(selectedTab, searchText)
        }

        Log.d("CallbackTab", "$selectedTab.title $selectedTab.selected")
    }

    private fun onGotPermissionsResultForFeature(grantedResult: Boolean) {
        if (grantedResult) {
            refreshData()

        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("perm", "not gra forever")
                //если запретили навсегда
                askUserForOpeningSettings()
                refreshLayout.isRefreshing = false
            } else {
                Log.d("perm", "not gra NOT forever")
                showDialogWithExplanationAndRequest()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun refreshData() {
        selectedTab = null
        shopsViewModel.clearData()
        shopsViewModel.refreshDataSearch(selectedTab, searchText)
    }

    private fun askUserForOpeningSettings() {
        val appsettingIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, null)
        )

        if (requireActivity().packageManager.resolveActivity(
                appsettingIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            Toast.makeText(requireContext(), "permissions are denied forever :(", Toast.LENGTH_LONG)
                .show()
        } else {
            AlertDialog.Builder(requireContext()).setTitle("Permissions denied")
                .setMessage("Would you like to open settings?")
                .setPositiveButton("open") { _, _ ->
                    startActivity(appsettingIntent)
                }
                .create()
                .show()
        }
    }

    private fun showDialogWithExplanationAndRequest() {
        Toast.makeText(requireContext(), "permissions are need for works", Toast.LENGTH_LONG)
            .show()
    }
}