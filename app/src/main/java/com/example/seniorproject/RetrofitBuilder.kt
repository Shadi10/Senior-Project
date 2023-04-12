package com.example.seniorproject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://bpay.binanceapi.com"

class RetrofitBuilder {
    var retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val binanceApi: BinancePayApi = retrofit!!.create(BinancePayApi::class.java)
}


