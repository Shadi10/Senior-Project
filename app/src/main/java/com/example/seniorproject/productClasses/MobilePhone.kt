package com.example.seniorproject.productClasses

import com.example.seniorproject.interfaces.Products

class MobilePhone(
    override val brands: String, override val itemId: Int,
) : Products {

    companion object {
        private var brandItemId = 0

        private val mobilePhoneBrands = listOf("Redmi",
            "Apple", "Asus", "Alcatel", "oppo", "acer", "Lg", "Hp",
            "HTC",
            "Infinix",
            "Panasonic",
            "BlackBerry",
            "Poco",
            "Techno",
            "TCL",
            "Google",
            "Doogee",
            "Realme",
            "Samsung",
            "Sony",
            "Xiaomi",
            "Vivo",
            "Philips",
            "Cat",
            "Lenovo",
            "OtherBrands",
            "Microsoft",
            "Motorola",
            "Nokia",
            "Hisense",
            "Huawei",
            "OnePlus",
            "Oukitel")

        fun createPhoneCategory(): ArrayList<MobilePhone> {
            val phones = ArrayList<MobilePhone>()
            for (i in 1..mobilePhoneBrands.size) {
                phones.add(MobilePhone(mobilePhoneBrands[i - 1], ++brandItemId))
            }
            return phones
        }
    }
}