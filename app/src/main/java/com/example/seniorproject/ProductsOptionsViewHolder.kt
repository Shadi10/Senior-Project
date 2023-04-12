package com.example.seniorproject

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.interfaces.RecyclerViewInterface

class ProductsOptionsViewHolder(
    private val view: View, private val recyclerViewInterface: RecyclerViewInterface,
) : RecyclerView.ViewHolder(view) {

    var productPic: ImageView = itemView.findViewById(R.id.productPicture)
    var productType: TextView = itemView.findViewById(R.id.productGenre)
    var arrowIconForward: ImageView = itemView.findViewById(R.id.arrowNext)
    var cardView: CardView = itemView.findViewById(R.id.card_view)
    // Handles the row being being clicked

    fun setViews(productGenre: ProductCategory) {
        view.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != productGenre.categoryId) {
                recyclerViewInterface.onItemClick(productGenre)
            }
        }
        cardView.animation = AnimationUtils.loadAnimation(view.context, R.anim.fade_list)

        productPic.layoutParams.height = 170
        productPic.layoutParams.width = 170

        productType.text = productGenre.categoryName
        productPic.setImageResource(productGenre.categoryImage)
        arrowIconForward.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
    }
}