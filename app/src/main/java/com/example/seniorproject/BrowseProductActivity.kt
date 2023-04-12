package com.example.seniorproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.databinding.BrowseActivityRecyclerviewBinding
import com.example.seniorproject.interfaces.HomeRecyclerViewInterface

class BrowseProductActivity : AppCompatActivity(), HomeRecyclerViewInterface {
    private lateinit var binding: BrowseActivityRecyclerviewBinding
    private val viewModel by viewModels<ProductViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BrowseActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences =
            this.getSharedPreferences("categoryNameAndImage", Context.MODE_PRIVATE)
        val categoryName = sharedPreferences.getString("categoryName", "Vehicles")
        binding.browseToolbar.title = categoryName
        if (categoryName != null) {
            viewModel.getProductsByCategory(categoryName)
        }
        viewModel.productLiveData.observe(this) { productlist ->
            setUpRecyclerView(productlist)
        }
        binding.browseToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.browseToolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpRecyclerView(productList: List<Product>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = BrowseActivityAdapter(productList, this)
        if (BrowseActivityAdapter(productList, this).itemCount == 0) {
            binding.tvNoCurrentPosts.visibility = View.VISIBLE
        } else {
            binding.tvNoCurrentPosts.visibility = View.INVISIBLE
        }
    }

    override fun onItemClick(product: Product) {
        val sharedPreferences = getSharedPreferences(
            "usernameAndId", MODE_PRIVATE
        )
        val userId = sharedPreferences.getInt("userId", 0)
        viewModel.getProductId(userId)
        if (product.userId != userId) {
            viewModel.getData(product)
        } else {
            Toast.makeText(this, "You cannot buy  your own product", Toast.LENGTH_SHORT).show()
        }
    }
}