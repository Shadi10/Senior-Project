package com.example.seniorproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.interfaces.HomeRecyclerViewInterface

class BrowseActivityAdapter(private val listOfProducts: List<Product>,private val recyclerViewInterface: HomeRecyclerViewInterface) :
    RecyclerView.Adapter<ProductsViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProductsViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.activity_browse_product, viewGroup, false)
        return ProductsViewHolder(v, recyclerViewInterface)

    }

    override fun onBindViewHolder(viewHolder: ProductsViewHolder, i: Int) {
        viewHolder.setViews(listOfProducts[i])
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }
}
