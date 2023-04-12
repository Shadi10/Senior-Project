package com.example.seniorproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.ProductCategory
import com.example.seniorproject.ProductsOptionsViewHolder
import com.example.seniorproject.R
import com.example.seniorproject.interfaces.RecyclerViewInterface

class ProductsAdapter(
    private val productsCategory: List<ProductCategory>,
    private val recyclerViewInterface: RecyclerViewInterface,
) : RecyclerView.Adapter<ProductsOptionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsOptionsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_offers, parent, false)
        return ProductsOptionsViewHolder(view, recyclerViewInterface)
    }

    override fun onBindViewHolder(holder: ProductsOptionsViewHolder, position: Int) {
        val product: ProductCategory = productsCategory[position]
        holder.setViews(product)

    }

    override fun getItemCount(): Int {
        return productsCategory.size
    }
}