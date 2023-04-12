package com.example.seniorproject.productClasses
import com.example.seniorproject.interfaces.Products

class FashionAndBeauty(
    override val brands: String,
    override val itemId: Int,
) :
    Products {

    companion object {
        private var brandItemId = 0
        private val fashionAndBeautyList = listOf("Clothing for Men",
            "Accessories for Men",
            "Clothing for Women",
            "Accessories for Women",
            "Make-up & Cosmetics",
            "Jewelry & Faux-Bijoux",
            "Watches",
            "Luggage")


        fun createFashionAndBeautyCategory(): ArrayList<FashionAndBeauty> {
            val fashionList = ArrayList<FashionAndBeauty>()
            for (i in 1..fashionAndBeautyList.size) {
                fashionList.add(FashionAndBeauty(fashionAndBeautyList[i - 1], ++brandItemId))
            }
            return fashionList
        }
    }
}