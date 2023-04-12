package com.example.seniorproject.productClasses

import com.example.seniorproject.interfaces.Products

class Pet(override val brands: String, override val itemId: Int) :
    Products {

    companion object {
        private var brandItemId = 0
        private val petBrand = listOf("Animal & Pet accessories",
            "Dogs",
            "Cats",
            "Birds",
            "Livestock",
            "Horses",
            "Fish",
            "Other Animal & Pets")

        fun createPetCategory(): ArrayList<Pet> {
            val pets = ArrayList<Pet>()
            for (i in 1..petBrand.size) {
                pets.add(Pet(petBrand[i - 1], ++brandItemId))
            }
            return pets
        }
    }
}