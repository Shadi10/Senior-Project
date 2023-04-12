package com.example.seniorproject.data.product

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey(autoGenerate = true) val productId: Int,
    val userName: String,
    var productImage: Uri,
    var productTitle: String,
    var productPrice: Double,
    val userId: Int,
    val dateOfPublish: String,
    val productCategoryName: String,
    var productDesc: String
)



