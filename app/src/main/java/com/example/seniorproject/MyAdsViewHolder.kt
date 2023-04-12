package com.example.seniorproject

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.data.product.ProductRepository
import com.example.seniorproject.data.user.UserDatabase
import com.example.seniorproject.fragments.DialogFragment


class MyAdsViewHolder(
    private val view: View,
    private val productViewModel: ProductViewModel,
    private val supportFragmentManager: FragmentManager,
) : RecyclerView.ViewHolder(view) {
    private val pics: ImageView = view.findViewById(R.id.productImage)
    private val productTitle: TextView = view.findViewById(R.id.title)
    private val productPrice: TextView = view.findViewById(R.id.productPrice)
    private val dateOfPublish: TextView = itemView.findViewById(R.id.dateOfPublish)
    private val options: ImageView = itemView.findViewById(R.id.threeDotsOption)
    private val dialogFragment = DialogFragment()

    @SuppressLint("DiscouragedApi", "SetTextI18n")

    private val repository: ProductRepository

    init {
        val userDao = UserDatabase.getDatabase(view.context.applicationContext).productDao()
        repository = ProductRepository(userDao)
    }

    @SuppressLint("SetTextI18n", "DiscouragedPrivateApi", "NotifyDataSetChanged")
    fun setViews(product: Product) {

        options.setOnClickListener {
            popUpMenu(product)
        }
        val sharedPreferences =
            view.context.getSharedPreferences("productEdit", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        dateOfPublish.text = product.dateOfPublish
        pics.setImageURI(product.productImage)
        productPrice.text = "USD " + product.productPrice.toString()
        productTitle.text = product.productTitle
        editor.putString("productPrice", product.productPrice.toString())
        editor.putString("productTitle", product.productTitle)
        editor.putString("productDescription", product.productDesc)
        editor.apply()
    }

    private fun popUpMenu(product: Product) {

        val sharedPreferences =
            view.context.getSharedPreferences("productIdPref", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putInt("productId", product.productId)
            apply()
        }
        val popup = PopupMenu(view.context, options)
        popup.inflate(R.menu.post_menu_options)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.deletePost -> {
                    AlertDialog.Builder(view.context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            productViewModel.deleteProduct(product)
                            Toast.makeText(view.context, "Post Deleted ", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }.setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }.create().show()

                    true
                }
                R.id.editPost -> {
                    dialogFragment.show(supportFragmentManager, "dialogFragment")
                    true
                }
                else -> true
            }
        }
        popup.show()
    }
}

