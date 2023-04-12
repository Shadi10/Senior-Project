package com.example.seniorproject.data.product

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ProductDao {

    @Insert
    suspend fun addProduct(products: Product)

    @Query("Select * from product_table order by dateOfPublish DESC  ")
    fun getProducts(): LiveData<List<Product>>

    @Update
    fun updateProduct(product: Product)

    @Query("Select * from product_table where userId=:userId order by dateOfPublish DESC")
    fun getProductId(userId: Int): LiveData<List<Product>>

    @Query("Select * from product_table where productId=:prodId")
    fun getProduct(prodId:Int):LiveData<List<Product>>
    @Delete
    fun deleteProduct(products: Product)

    @Query("SELECT * FROM product_table where userId=:userId")
    fun getData(userId: Int): LiveData<List<Product>>

    @Query("SELECT * from product_table where productCategoryName=:prodCategoryName order by dateOfPublish DESC")
    fun getProductsByCategory(prodCategoryName: String): LiveData<List<Product>>

}