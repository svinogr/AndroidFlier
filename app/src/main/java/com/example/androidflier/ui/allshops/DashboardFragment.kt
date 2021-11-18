package com.example.androidflier.ui.allshops

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
import com.example.androidflier.databinding.FragmentDashboardBinding
import com.example.androidflier.model.Shop
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
        Log.d("ref", "onCreateView")
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)



        setRefreshLayout()
        setRecyclerView()

        /*     refreshLayout.post{
                 Log.d("ref", "post")

                 refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
                 // onRefresh()
                 shopsViewModel.refreshData()
             }*/
        //onRefresh()

       /* if (shopsViewModel.shops.value?.size == 0) {
            onRefresh()
        }
*/
        refreshLayout.post {
            Log.d("ref", "post")

            refreshLayout.isRefreshing = true // чтобы появился прогрес бар на начальном этапе
             onRefresh()
         //  shopsViewModel.refreshData()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ref", "onCreate")
        shopsViewModel = ViewModelProvider(
            requireActivity(),
            ListModelFactory(this.requireContext())
        ).get(TAG, DashboardViewModel::class.java)



    }

    override fun onResume() {
        super.onResume()
        Log.d("ref", "onResume")
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
    }

    private fun setObserver() {
        shopObserver = Observer {
            if(viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {

                Log.d("ref", "rec1 ${adapter.listShops.size}")
                adapter.listShops = it
                adapter.notifyDataSetChanged()
                recyclerView.scheduleLayoutAnimation()
                Log.d("ref", "rec2 ${adapter.listShops.size}")
                Log.d("ref", "setRecyclerView")
                Log.d("ref", "rec $it")
                refreshLayout.isRefreshing = false // без этого не закроется прогрес бар
            }


        }

        shopsViewModel.shops.observe(
            viewLifecycleOwnerLiveData.value!!, shopObserver
        )
    }

    override fun onStop() {
        //  adapter.listShops = mutableListOf()
        // adapter.notifyDataSetChanged()
        super.onStop()
        Log.d("ref", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ref", "onDestroyView")
       // shopsViewModel.shops.removeObserver(shopObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ref", "onDestroy")
    }

    override fun onRefresh() {
        refreshLayout.isRefreshing = true
        Log.d("ref", "onRefresh")
        shopsViewModel.refreshData()
    }
}