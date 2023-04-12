package com.example.seniorproject.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.MainActivity
import com.example.seniorproject.ProductViewModel
import com.example.seniorproject.R
import com.example.seniorproject.adapter.MyAdsAdapter
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.databinding.FragmentMyaddsBinding

class MyAdsFragment : Fragment() {
    private lateinit var binding: FragmentMyaddsBinding
    private val viewModel by activityViewModels<ProductViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyaddsBinding.inflate(inflater, container, false)

        (activity as MainActivity).supportActionBar?.title = getString(R.string.my_adds_title)
        val prefUserId: SharedPreferences? =
            this.activity?.getSharedPreferences("usernameAndId", AppCompatActivity.MODE_PRIVATE)
        val id = prefUserId?.getInt("userId", 0)
        if (id != null) {
            viewModel.getProductId(id)
        }

        viewModel.myProductsLiveData.observe(viewLifecycleOwner) { productlist ->
            setUpRecyclerView(productlist)
        }

        return binding.root
    }

    private fun setUpRecyclerView(productList: List<Product>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this.activity)
        binding.recyclerView.adapter = MyAdsAdapter(productList, viewModel, childFragmentManager)
        if (MyAdsAdapter(productList, viewModel, childFragmentManager).itemCount == 0) {
            binding.tvNoCurrentAds.visibility = View.VISIBLE
        } else {
            binding.tvNoCurrentAds.visibility = View.INVISIBLE
        }
    }
}
