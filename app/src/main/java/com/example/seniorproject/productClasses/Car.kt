package com.example.seniorproject.productClasses

import com.example.seniorproject.interfaces.Products


class Car(override val brands: String, override val itemId: Int) :
    Products {

    companion object {
        private var brandItemId = 0
        val carBrands = listOf(
            "Alfa Romeo",
            "Aston Martin",
            "Audi",
            "BMW",
            "Bugatti",
            "Buick",
            "Cadillac",
            "Chevrolet",
            "Chrysler",
            "Citroen",
            "Dacia",
            "Daewoo",
            "Daihatsu",
            "Datsun",
            "Dodge",
            "Ferrari",
            "Fiat",
            "Ford",
            "GMC",
            "Honda",
            "Hyundai",
            "Infiniti",
            "Jaguar",
            "Jeep",
            "Kia",
            "Lamborghini",
            "Lancia",
            "Land Rover",
            "Lexus",
            "Lincoln",
            "Lotus",
            "Maserati",
            "Mazda",
            "McLaren",
            "Mercedes-Benz",
            "Mini",
            "Mitsubishi",
            "Nissan",
            "Opel",
            "Peugeot",
            "Porsche",
            "Renault",
            "Rolls-Royce",
            "Saab",
            "Seat",
            "Skoda",
            "Smart",
            "SsangYong",
            "Subaru",
            "Suzuki",
            "Tata",
            "Tesla",
            "Toyota",
            "Volkswagen (VW)",
            "Volvo"
        )


        fun createCarCategory(): ArrayList<Car> {
            val cars = ArrayList<Car>()
            for (i in 1..carBrands.size) {
                cars.add(Car(carBrands[i - 1], ++brandItemId))
            }
            return cars
        }
    }
}