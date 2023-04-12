package com.example.seniorproject

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.interfaces.RecyclerViewInterface

class BrowseCategoryViewHolder(
    private val view: View, private val recyclerViewInterface: RecyclerViewInterface,
) : RecyclerView.ViewHolder(view) {

    var productPic: ImageView = itemView.findViewById(R.id.browse_productPicture)
    var cardView: CardView = itemView.findViewById(R.id.browse_category_cardView)
    var categoryName: TextView = itemView.findViewById(R.id.browse_categoryName)
    fun setViews(productGenre: ProductCategory) {
        view.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != productGenre.categoryId) {
                recyclerViewInterface.onItemClick(productGenre)
            }
        }
        categoryName.text = productGenre.categoryName
        productPic.layoutParams.height = 170
        productPic.layoutParams.width = 170
        productPic.setImageResource(productGenre.categoryImage)
    }
}