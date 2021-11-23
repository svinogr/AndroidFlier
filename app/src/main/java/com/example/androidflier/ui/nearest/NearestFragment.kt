package com.example.androidflier.ui.nearest

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidflier.R
import com.example.androidflier.adapter.ShopCardAdapter
import com.example.androidflier.adapter.TabCardAdapter
import com.example.androidflier.adapter.holder.TabViewHolder
import com.example.androidflier.databinding.FragmentNearestBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.ui.viewmodels.ListModelFactory

class NearestFragment : Fragment(R.layout.fragment_nearest), SwipeRefreshLayout.OnRefreshListener, TabViewHolder.TabSelectable {

    private var _binding: FragmentNearestBinding? = null
    private val requestFeatureLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onGotPermissionsResultForFeature
    )

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewTab: RecyclerView
    private lateinit var shopsViewModel: NearestListShopsViewModel
    private lateinit var adapter: ShopCardAdapter
    private lateinit var adapterTab: TabCardAdapter
    private lateinit var shopObserver: Observer<List<Shop>>
    private lateinit var messageObserver: Observer<String>
    private lateinit var tabsObserver: Observer<List<Tab>>
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
        setSettings()

        refreshLayout.post {
            refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
            onRefresh()
        }

        return binding.root
    }

    private fun setSettings() {
        setRecyclerView()
        setRefreshLayout()
        setObservers()
    }

    private fun onGotPermissionsResultForFeature(grantedResult: Boolean) {
        if (grantedResult) {
            shopsViewModel.refreshData()
          //  refreshLayout.isRefreshing = false

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

    private fun setRecyclerView() {
        recyclerView = binding.nearestRecyclerView
        val lManager = LinearLayoutManager(view?.context)
        recyclerView.layoutManager = lManager

        adapter = ShopCardAdapter()
        recyclerView.adapter = adapter

        recyclerViewTab = binding.nearestRecyclerTabView
        val TLManager = LinearLayoutManager(view?.context)
        TLManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewTab.layoutManager = TLManager

        adapterTab = TabCardAdapter(tabSelectable = this)
        recyclerViewTab.adapter = adapterTab
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


        tabsObserver = Observer {
            Log.d("DashboardViewModel state", viewLifecycleOwner.lifecycle.currentState.toString())
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Log.d("TAg", it.toString())
                adapterTab.listTab = it
                adapterTab.notifyDataSetChanged()
                recyclerViewTab.scheduleLayoutAnimation()
            }
        }

        shopsViewModel.tabs.observe(viewLifecycleOwner, tabsObserver)
    }

    private fun setRefreshLayout() {
        refreshLayout = binding.nearestRefreshLayout
        refreshLayout.setOnRefreshListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shopObserver.let { shopsViewModel.shops.removeObserver(shopObserver) }
         messageObserver.let { shopsViewModel.message.removeObserver(messageObserver)}
        _binding = null
    }

    override fun onRefresh() {
        Log.d("ref", "onRefresh")
        requestFeatureLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun withTab(tab: Tab) {
        Log.d("callbacktab", tab.title)
    }
}