package com.example.seniorproject

import com.binance.android.binancepay.api.BinancePayException
import com.binance.android.binancepay.api.BinancePayListener

class PayListener : BinancePayListener {
    override fun onSuccess() {
        // When the payment is successful, this will be called
    }

    override fun onCancel() {
        // When the payment is canceled, this will be called
    }

    override fun onError(exception: BinancePayException) {
        // When there is an error in the payment process，this will be called
    }
}

val listener = PayListener()
