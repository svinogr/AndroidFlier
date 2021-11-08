package com.example.androidflier.ui.allshops

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.androidflier.ui.viewmodels.ListModelFactory
import com.example.androidflier.ui.nearest.NearestListShopsViewModel

class DashboardFragment : Fragment(R.layout.fragment_dashboard), View.OnClickListener {
    private var _binding: FragmentNearestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopsViewModel: DashboardViewModel
    private lateinit var adapter: ShopCardAdapter
    private lateinit var shopObserver: Observer<List<Shop>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestBinding.inflate(inflater, container, false)
        shopsViewModel = ViewModelProvider(requireActivity(), ListModelFactory()).get("2", DashboardViewModel::class.java)

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

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //  shopsViewModel.allShopsTest()

        Log.i("ttttt", "res")
    }

    override fun onClick(v: View?) {
       // shopsViewModel.allNearestShops()
    /*    Toast.makeText(
            activity,
            "button ${shopsViewModel.shops.value?.size}",
            Toast.LENGTH_SHORT
        )
            .show()*/
    }
}