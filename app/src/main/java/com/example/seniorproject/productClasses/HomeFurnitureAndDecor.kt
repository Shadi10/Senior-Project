package com.example.seniorproject.productClasses

import com.example.seniorproject.interfaces.Products

class HomeFurnitureAndDecor(override val brands: String, override val itemId: Int) :
    Products {

    companion object {
        private var brandItemId = 0
        private val homeFurnitureAndDecor = listOf("Living Room",
            "Bedroom",
            "Dining Room",
            "Kitchen & Kitchenware",
            "Bathroom",
            "Home Decoration & Accessories",
            "Garden & Outdoor",
            "Other Home Furniture & Decor")

        fun createHomeFurnitureAndDecorCategory(): ArrayList<HomeFurnitureAndDecor> {
            val homeFurnitureAndDecorList = ArrayList<HomeFurnitureAndDecor>()
            for (i in 1..homeFurnitureAndDecor.size) {
                homeFurnitureAndDecorList.add(HomeFurnitureAndDecor(homeFurnitureAndDecor[i - 1],
                    ++brandItemId))
            }
            return homeFurnitureAndDecorList
        }
    }
}