package com.example.seniorproject

data class OrderResponse(
    val status: String, // SUCCESS
    val code: String, // 000000
    val data: Data,
    val errorMessage: String,
)

