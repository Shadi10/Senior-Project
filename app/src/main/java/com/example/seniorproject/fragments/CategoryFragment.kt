package com.example.seniorproject.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.MainActivity
import com.example.seniorproject.ProductCategory
import com.example.seniorproject.R
import com.example.seniorproject.adapter.ProductsAdapter
import com.example.seniorproject.databinding.FragmentCategoryBinding
import com.example.seniorproject.interfaces.RecyclerViewInterface

class CategoryFragment : Fragment(), RecyclerViewInterface {
    private lateinit var binding: FragmentCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val productsCategory: ArrayList<ProductCategory> = ProductCategory.createCategoryList(7)
        setUpRecyclerView(productsCategory)

        binding.categoryToolbar.setNavigationOnClickListener {
            val intent = Intent(this.context, MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun setUpRecyclerView(productCategory: List<ProductCategory>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this.activity)
        binding.recyclerView.adapter = ProductsAdapter(productCategory, this)
    }

    override fun onItemClick(productGenre: ProductCategory) {

        val productFragment = ProductFragment()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val sharedPreferences =
            requireActivity().getSharedPreferences("categoryNameAndImage", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply() {
            editor.putString("categoryName", productGenre.categoryName)
            editor.putInt("categoryImage", productGenre.categoryImage)
        }.apply()

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.frameLayout, productFragment).commit()
    }

}