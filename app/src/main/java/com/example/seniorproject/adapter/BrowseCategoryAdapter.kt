package com.example.seniorproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.BrowseCategoryViewHolder
import com.example.seniorproject.ProductCategory
import com.example.seniorproject.R
import com.example.seniorproject.interfaces.RecyclerViewInterface

class BrowseCategoryAdapter(
    private val productsCategory: List<ProductCategory>,
    private val recyclerViewInterface: RecyclerViewInterface,
) : RecyclerView.Adapter<BrowseCategoryViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BrowseCategoryViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.browse_category_layout, viewGroup, false)
        return BrowseCategoryViewHolder(v, recyclerViewInterface)

    }

    override fun onBindViewHolder(holder: BrowseCategoryViewHolder, position: Int) {
        val productCategory: ProductCategory = productsCategory[position]
        holder.setViews(productCategory)
    }

    override fun getItemCount(): Int {
        return productsCategory.size
    }

}