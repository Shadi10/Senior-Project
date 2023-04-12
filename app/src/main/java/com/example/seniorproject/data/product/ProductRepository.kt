package com.example.seniorproject.data.product

import androidx.lifecycle.LiveData

class ProductRepository(private val productDao: ProductDao) {

    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    fun getProducts(): LiveData<List<Product>> {
        return productDao.getProducts()
    }

    fun updateProduct(product: Product) {
        return productDao.updateProduct(product)
    }

    fun getProductId(userId: Int): LiveData<List<Product>> {
        return productDao.getProductId(userId)
    }
    fun getProduct(prodId:Int):LiveData<List<Product>>{
        return productDao.getProduct(prodId)
    }

    fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    fun getProductsByCategory(categoryProductName: String): LiveData<List<Product>> {
        return productDao.getProductsByCategory(categoryProductName)
    }
}