package com.example.seniorproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MyAdsViewHolder
import com.example.seniorproject.ProductViewModel
import com.example.seniorproject.R
import com.example.seniorproject.data.product.Product

class MyAdsAdapter(
    private val listOfProducts: List<Product>,
    private val productViewModel: ProductViewModel,
    private val supportFragmentManager: FragmentManager,
) :
    RecyclerView.Adapter<MyAdsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdsViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_ads_fragment_layout, parent, false)
        return MyAdsViewHolder(v, productViewModel, supportFragmentManager)
    }

    override fun onBindViewHolder(holder: MyAdsViewHolder, position: Int) {
        holder.setViews(listOfProducts[position])
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }

}