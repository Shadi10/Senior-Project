package com.example.seniorproject.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.*
import com.example.seniorproject.adapter.Adapter
import com.example.seniorproject.adapter.BrowseCategoryAdapter
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.interfaces.HomeRecyclerViewInterface
import com.example.seniorproject.interfaces.RecyclerViewInterface


class HomeFragment() : Fragment(), RecyclerViewInterface, HomeRecyclerViewInterface {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<ProductViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.home_title)
        val productsCategory: ArrayList<ProductCategory> = ProductCategory.createCategoryList(7)
        setUpCategoryRecyclerView(productsCategory)

        viewModel.getProduct()
        viewModel.productLiveData.observe(viewLifecycleOwner) { productlist ->
            setUpRecyclerView(productlist)
        }
        return binding.root
    }

    private fun setUpRecyclerView(productList: List<Product>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this.activity)
        binding.recyclerView.adapter = Adapter(productList, this)
        if (Adapter(productList, this).itemCount == 0) {
            binding.tvNoCurrentPosts.visibility = View.VISIBLE
        } else {
            binding.tvNoCurrentPosts.visibility = View.INVISIBLE
        }
    }

    private fun setUpCategoryRecyclerView(productCategoryList: List<ProductCategory>) {
        binding.categoryRecyclerView.layoutManager =
            LinearLayoutManager(this.activity, LinearLayoutManager.HORIZONTAL, false)
        binding.categoryRecyclerView.adapter = BrowseCategoryAdapter(productCategoryList, this)
    }

    override fun onItemClick(productGenre: ProductCategory) {

        val sharedPreferences =
            requireActivity().getSharedPreferences("categoryNameAndImage", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply() {
            editor.putString("categoryName", productGenre.categoryName)
            editor.putInt("categoryImage", productGenre.categoryImage)
        }.apply()

        val intent = Intent(requireActivity(), BrowseProductActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClick(product: Product) {

        val sharedPreferences = requireActivity().getSharedPreferences(
            "usernameAndId", AppCompatActivity.MODE_PRIVATE
        )

        val userId = sharedPreferences.getInt("userId", 0)
        viewModel.getProductId(userId)
        if (product.userId != userId) {
            viewModel.getData(product)
        }
    }
}
