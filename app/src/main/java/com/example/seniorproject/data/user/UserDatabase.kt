package com.example.seniorproject.data.user

import android.content.Context
import androidx.room.*
import com.example.seniorproject.UriConverters
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.data.product.ProductDao


@Database(entities = [User::class, Product::class], version = 2, exportSchema = false)
@TypeConverters(UriConverters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance

            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}