package com.example.seniorproject

import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface BinancePayApi {

    @POST("/binancepay/openapi/v2/order")
    fun createOrder(
        @Header("content-type") contentType: String,
        @Header("BinancePay-Timestamp") timestamp: Long,
        @Header("BinancePay-Nonce") nonce: String,
        @Header("BinancePay-Certificate-SN") apiKey: String,
        @Header("BinancePay-Signature") signature: String,
        @Body payload: Map<String, @JvmSuppressWildcards Any>
    ): Call<OrderResponse>

}