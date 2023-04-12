package com.example.seniorproject.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val name: String,
    val email: String,
    val mobileNumber: String,
    val password: String
    )
