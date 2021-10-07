package com.example.androidflier.ui.nearest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidflier.R
import com.example.androidflier.adapter.ShopCardAdapter
import com.example.androidflier.databinding.FragmentHomeBinding
import com.example.androidflier.model.Shop

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val list = listOf<Shop>(Shop(1, 10.0, 10.0, "Address", "descrip", "www", "url", "title"))
        val recycler = inflater.inflate(R.layout.fragment_home,)


        val adapter = ShopCardAdapter(list)
        adapter.return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}