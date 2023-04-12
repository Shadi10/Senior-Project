package com.example.seniorproject

data class Balance(
    val asset: String, // BUSD
    val available: Double, // 990000.00000000
    val availableBtcValuation: Double, // 22.98780000
    val availableFiatValuation: Double, // 989991.90516600
    val freeze: Int // 0E-8
)