package com.example.seniorproject.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.ProductsViewHolder
import com.example.seniorproject.R
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.interfaces.HomeRecyclerViewInterface


class Adapter(private val listOfProducts: List<Product>,private val recyclerViewInterface: HomeRecyclerViewInterface) :
    RecyclerView.Adapter<ProductsViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProductsViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_layout, viewGroup, false)
        return ProductsViewHolder(v,recyclerViewInterface)

    }

    override fun onBindViewHolder(viewHolder: ProductsViewHolder, i: Int) {
        viewHolder.setViews(listOfProducts[i])
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }
}