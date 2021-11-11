package com.example.androidflier.ui.nearest

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.adapter.ShopCardAdapter
import com.example.androidflier.databinding.FragmentNearestBinding
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.localdb.DataBaseHelper
import com.example.androidflier.repo.localdb.ManagerLocalStorage
import com.example.androidflier.ui.viewmodels.ListModelFactory

class NearestFragment : Fragment(R.layout.fragment_nearest) {

    //  private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentNearestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopsViewModel: NearestListShopsViewModel
    private lateinit var adapter: ShopCardAdapter
    private lateinit var shopObserver: Observer<List<Shop>>
    private lateinit var db: ManagerLocalStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNearestBinding.bind(view)
        shopsViewModel = ViewModelProvider(requireActivity(), ListModelFactory()).get(
            "1",
            NearestListShopsViewModel::class.java
        )

        db = ManagerLocalStorage(requireContext().applicationContext)

        recyclerView = binding.nearestRecyclerView
        val lManager = LinearLayoutManager(view?.context)
        recyclerView.layoutManager = lManager

        adapter = ShopCardAdapter()
        recyclerView.adapter = adapter

        shopObserver = Observer {
            adapter.listShops = it
            adapter.notifyDataSetChanged()
            Toast.makeText(
                this.context,
                shopsViewModel.shops.value?.size.toString(),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        shopsViewModel.shops.observe(
            viewLifecycleOwner, shopObserver
        )
    }
}