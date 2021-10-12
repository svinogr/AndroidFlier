package com.example.androidflier.ui.nearest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.adapter.ShopCardAdapter
import com.example.androidflier.databinding.FragmentNearestBinding
import com.example.androidflier.ui.viewmodels.ShopsViewModel

class NearestFragment : Fragment() {

    //  private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentNearestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopsViewModel: ShopsViewModel
    private lateinit var adapter: ShopCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestBinding.inflate(inflater, container, false)
        shopsViewModel = ViewModelProvider(this).get(ShopsViewModel::class.java)

        recyclerView = binding.nearestRecyclerView
        val lManager = LinearLayoutManager(view?.context)
        recyclerView.layoutManager = lManager

        adapter = ShopCardAdapter()
        recyclerView.adapter = adapter

        updateUi()

        return binding.root
    }

    fun updateUi() {
        adapter.listShops = shopsViewModel.getAllShops()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
         _binding = null
    }
}