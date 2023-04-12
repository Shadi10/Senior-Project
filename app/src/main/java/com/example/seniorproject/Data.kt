package com.example.seniorproject

import java.net.URI

data class Data(
    val prepayId: String, // 29383937493038367292
    val terminalType: String, // APP
    val expireTime: Long, // 121123232223
    val qrcodeLink: String, // https://qrservice.dev.com/en/qr/dplkb005181944f84b84aba2430e1177012b.jpg
    val qrContent: String, // https://qrservice.dev.com/en/qr/dplk12121112b
    val checkoutUrl: URI, // https://pay.binance.com/checkout/dplk12121112b
    val deeplink: String, // bnc://app.binance.com/payment/secpay/xxxxxx
    val universalUrl: String // https://app.binance.com/payment/secpay?_dp=xxx=&linkToken=xxx
)
