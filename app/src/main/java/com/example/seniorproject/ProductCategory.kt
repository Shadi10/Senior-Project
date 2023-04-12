package com.example.seniorproject

class  ProductCategory(val categoryName: String, val categoryId: Int, val categoryImage: Int) {

    companion object {
        private var categoryId = 0
        private var categoryName = listOf("Vehicles",
            "Mobile Phones",
            "Electronics",
            "Sports & Equipment",
            "Pets",
            "Fashion & Beauty",
            "Home Furniture & Decor")
        private var categoryImage = listOf(R.drawable.carrs,
            R.drawable.phone,
            R.drawable.electronics,
            R.drawable.ball,
            R.drawable.dog,
            R.drawable.fashion,
            R.drawable.couch)

        fun createCategoryList(numCategory: Int): ArrayList<ProductCategory> {
            val contacts = ArrayList<ProductCategory>()
            for (i in 1..numCategory) {
                contacts.add(ProductCategory(categoryName[i - 1],
                    ++categoryId,
                    categoryImage[i - 1]))
            }
            return contacts
        }
    }
}