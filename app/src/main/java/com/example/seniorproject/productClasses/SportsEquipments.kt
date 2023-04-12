package com.example.seniorproject.productClasses

import com.example.seniorproject.interfaces.Products

class SportsEquipments(
    override val brands: String,
    override val itemId: Int,
) :
    Products {

    companion object {
        private var brandItemId = 0
        private val sportsList = listOf("Bicycles & Accessories",
            "Outdoors & Camping",
            "Gym,Fitness & Fighting sports",
            "Ball Sports",
            "Billiard & Similar Games",
            "Ski & Winter Sports",
            "Water Sports & Diving",
            "Tennis & Racket Sports",
            "Other Sports")

        fun createSportCategory(): ArrayList<SportsEquipments> {
            val sports = ArrayList<SportsEquipments>()
            for (i in 1..sportsList.size) {
                sports.add(SportsEquipments(sportsList[i - 1], ++brandItemId))
            }
            return sports
        }
    }
}