package com.example.seniorproject

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.interfaces.ProductRecyclerViewInterface
import com.example.seniorproject.interfaces.Products

class ProductViewHolder(
    private val view: View,
    private val recyclerViewInterface: ProductRecyclerViewInterface,
) : RecyclerView.ViewHolder(view) {

    var productBrand: TextView = itemView.findViewById(R.id.productBrand)
    var arrowIconForward: ImageView = itemView.findViewById(R.id.arrowNext)
    var cardView: CardView = itemView.findViewById(R.id.car_card_view)

    fun setViews(product: Products) {
        view.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != product.itemId) {
                recyclerViewInterface.onItemClick(product)
            }
        }
        cardView.animation = AnimationUtils.loadAnimation(view.context, R.anim.fade_list)
        productBrand.text = product.brands
        arrowIconForward.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
    }
}