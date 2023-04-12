package com.example.seniorproject.productClasses

import com.example.seniorproject.interfaces.Products

class Electronics(override val brands: String, override val itemId: Int) :
    Products {

    companion object {
        private var brandItemId = 0
        private val electronics = listOf("Tv & Video",
            "Home Audio & Speakers",
            "Ac,Cooling & Heating",
            "Laptops,Tablets & Computers",
            "Computer Parts & IT Accessories",
            "Cameras",
            "Gaming Consoles & Accessories",
            "Video Games",
            "Other Electronics")


        fun createElectronicsCategory(): ArrayList<Electronics> {
            val electronicsList = ArrayList<Electronics>()
            for (i in 1..electronics.size) {
                electronicsList.add(Electronics(electronics[i - 1],
                    ++brandItemId))
            }
            return electronicsList
        }
    }
}
