package com.example.seniorproject

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.data.product.ProductRepository
import com.example.seniorproject.data.user.UserDatabase
import com.example.seniorproject.interfaces.HomeRecyclerViewInterface


class ProductsViewHolder(
    private val view: View, private val recyclerViewInterface: HomeRecyclerViewInterface
) : RecyclerView.ViewHolder(view) {
    private val pics: ImageView = view.findViewById(R.id.productImage)
    private val username: TextView = view.findViewById(R.id.username)
    private val productTitle: TextView = view.findViewById(R.id.title)
    private val productPrice: TextView = view.findViewById(R.id.productPrice)
    private val bookmarkUnchecked: ImageView = itemView.findViewById(R.id.bookmark_post)
    private val bookmarkChecked: ImageView = itemView.findViewById(R.id.bookmark_checked_post)
    private val dateOfPublish: TextView = itemView.findViewById(R.id.dateOfPublish)
    private val postDescription: TextView = itemView.findViewById(R.id.tv_post_description)
    private val buyProductBtn: Button = itemView.findViewById(R.id.buyProductBtn)

    private val repository: ProductRepository

    init {
        val userDao = UserDatabase.getDatabase(view.context.applicationContext).productDao()
        repository = ProductRepository(userDao)
    }

    @SuppressLint("SetTextI18n", "DiscouragedPrivateApi", "NotifyDataSetChanged")
    fun setViews(product: Product) {

        val sharedPreferences = view.context.getSharedPreferences(
            "usernameAndId", AppCompatActivity.MODE_PRIVATE
        )
        val userId = sharedPreferences.getInt("userId", 0)

        if (product.userId == userId) {
            buyProductBtn.visibility = View.GONE
        }
        buyProductBtn.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != product.productId) {
                recyclerViewInterface.onItemClick(product)
            } else {
                recyclerViewInterface.onItemClick(product)
            }
        }
        bookmarkUnchecked.setOnClickListener {

            Toast.makeText(view.context, "Post saved", Toast.LENGTH_SHORT).show()
            bookmarkUnchecked.visibility = View.INVISIBLE
            bookmarkChecked.visibility = View.VISIBLE

        }
        bookmarkChecked.setOnClickListener {

            Toast.makeText(view.context, "Post unsaved", Toast.LENGTH_SHORT).show()
            bookmarkUnchecked.visibility = View.VISIBLE
            bookmarkChecked.visibility = View.INVISIBLE
        }
        val imageUrl = product.productImage

        val sharedOptions: RequestOptions =
            RequestOptions().placeholder(R.mipmap.ic_launcher).circleCrop()


        dateOfPublish.text = product.dateOfPublish
        pics.setImageURI(product.productImage)
        username.text = product.userName
        productPrice.text = "USD " + product.productPrice.toString()
        productTitle.text = product.productTitle
        postDescription.text = product.productDesc
    }

}

