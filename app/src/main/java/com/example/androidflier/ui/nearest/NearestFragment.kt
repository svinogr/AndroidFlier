package com.example.androidflier.ui.nearest

import android.os.Bundle
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
import com.example.androidflier.ui.viewmodels.ListModelFactory

class NearestFragment : Fragment(R.layout.fragment_nearest), View.OnClickListener {

    //  private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentNearestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopsViewModel: NearestListShopsViewModel
    private lateinit var adapter: ShopCardAdapter
    private lateinit var shopObserver: Observer<List<Shop>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNearestBinding.bind(view)
        shopsViewModel = ViewModelProvider(requireActivity(), ListModelFactory()).get(
            "1",
            NearestListShopsViewModel::class.java
        )

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

/*    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestBinding.inflate(inflater, container, false)

        val location = Location("HZ cho eto") // TODO заменить на настоящую location
        location.latitude = 10.0
        location.longitude = 10.0
        //shopsViewModel = ViewModelProvider(requireActivity()).get("1", ShopsViewModel::class.java)
        shopsViewModel = ViewModelProvider(requireActivity(), ShopModelFactory(requireContext())).get("1", ShopsViewModel::class.java)

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

        shopsViewModel.getLocationData().observe(viewLifecycleOwner, Observer {
            Toast.makeText(
                this.context,
           it.longitude.toString(),
                Toast.LENGTH_SHORT
            )
                .show()
        })

        binding.btn.setOnClickListener(
            this
        )
  //      shopsViewModel.allShopsTest()
        Log.i("ttttt", "cre")

        // updateUi()
        return binding.root
    }*/
/*
    override fun onResume() {
        super.onResume()

      //  shopsViewModel.allShopsTest()

        Log.i("ttttt", "res")
    }*/

/* не
    fun updateUi() {
        adapter.listShops = shopsViewModel.allShopsTest()
        adapter.notifyDataSetChanged()
    }*/

/*    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/

    override fun onClick(v: View?) {
        shopsViewModel.allNearestShops()
        Toast.makeText(
            activity,
            "button ${shopsViewModel.shops.value?.size}",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}