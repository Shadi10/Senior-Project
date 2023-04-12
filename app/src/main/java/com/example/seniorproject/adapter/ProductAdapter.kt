package com.example.seniorproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.ProductViewHolder
import com.example.seniorproject.R
import com.example.seniorproject.interfaces.ProductRecyclerViewInterface
import com.example.seniorproject.interfaces.Products

class ProductAdapter(
    private val product: List<Products>, private val recyclerViewInterface: ProductRecyclerViewInterface,
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_brands_layout, parent, false)
        return ProductViewHolder(view, recyclerViewInterface)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: Products = product[position]
        product.brands
        holder.setViews(product)
    }

    override fun getItemCount(): Int {
        return product.size
    }
}