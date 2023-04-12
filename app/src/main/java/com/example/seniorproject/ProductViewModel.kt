package com.example.seniorproject

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.binance.android.binancepay.api.BinancePayFactory
import com.binance.android.binancepay.api.BinancePayParam
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.data.product.ProductRepository
import com.example.seniorproject.data.user.UserDatabase
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Hex
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class ProductViewModel(application: Application) : AndroidViewModel(application) {

    val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("usernameAndId", AppCompatActivity.MODE_PRIVATE)

    private val FIRSTACCOUNT = "amineshadi10@gmail.com"
    private val SECONDACCOUNT = "shadiamin04@gmail.com"
    private val merchantApiKey =
        when (sharedPreferences.getString("userEmail", "amineshadi10@gmail.com")) {
            FIRSTACCOUNT -> {
                getMerchantAPIKEY(
                    "phewgzqlvg26hveertlrlq2bec21vfvherl88acrwlmyd1b2xmp5y8ndesd8o0ka"
                )
            }
            SECONDACCOUNT -> {
                getMerchantAPIKEY(
                    "e4wxpa6veph5h98sl9pidnobmcclgarbv0flhfjncts6ycoap3zpsvxgdapjvh0v"
                )
            }
            else -> {
                getMerchantAPIKEY(
                    "phewgzqlvg26hveertlrlq2bec21vfvherl88acrwlmyd1b2xmp5y8ndesd8o0ka"
                )
            }
        }
    private val merchantSecretKey =
        when (sharedPreferences.getString("userEmail", "amineshadi10@gmail.com")) {
            FIRSTACCOUNT -> {
                getMerchantSecretKey(
                    "###############################"
                )
            }
            SECONDACCOUNT -> {
                getMerchantSecretKey(
                    "##################################"
                )
            }
            else -> {
                getMerchantSecretKey("######################################")
            }
        }
    private val merchantId =
        when (sharedPreferences.getString("userEmail", "amineshadi10@gmail.com")) {
            FIRSTACCOUNT -> {
                getMerchantId(
                    587044602
                )
            }
            SECONDACCOUNT -> {
                getMerchantId(
                    370622621
                )
            }
            else -> {
                getMerchantId(587044602)
            }
        }
    private var timestamp: Long? = null
    var productLiveData: LiveData<List<Product>> = MutableLiveData()
    var prepayId: String = ""
    var myProductsLiveData: LiveData<List<Product>> = MutableLiveData()
    private val repository: ProductRepository

    init {
        val userDao = UserDatabase.getDatabase(application).productDao()
        repository = ProductRepository(userDao)
    }

    fun createHmacSignature(
        payload: String, secret: String
    ): String {
        return try {
            val secretKey = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), "HmacSHA512")
            val mac = Mac.getInstance("HmacSHA512")
            mac.init(secretKey)
            val hmac = mac.doFinal(payload.toByteArray(StandardCharsets.UTF_8))
            Hex.encodeHexString(hmac)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("Invalid algorithm: HmacSHA512")
        } catch (e: InvalidKeyException) {
            throw IllegalArgumentException("Invalid API secret")
        }
    }

    private fun getMerchantAPIKEY(apiKey: String): String {
        return apiKey
    }

    private fun getMerchantSecretKey(secretKey: String): String {
        return secretKey
    }

    private fun getMerchantId(id: Int): Int {
        return id
    }

    fun getData(product: Product) {
        val job = CoroutineScope(Dispatchers.IO).async {
            val ntpServer = "pool.ntp.org"
            val ntpPort = 123
            val timeout = 5000

            val socket = withContext(Dispatchers.IO) {
                DatagramSocket().also {
                    it.connect(InetAddress.getByName(ntpServer), ntpPort)
                }
            }
            socket.soTimeout = timeout

            val request = ByteArray(48)
            request[0] = 0x1B
            val packet = DatagramPacket(
                request, request.size, withContext(Dispatchers.IO) {
                    InetAddress.getByName(ntpServer)
                }, ntpPort
            )
            withContext(Dispatchers.IO) {
                socket.send(packet)
            }

            val response = ByteArray(48)
            val responsePacket = DatagramPacket(response, response.size)
            withContext(Dispatchers.IO) {
                socket.receive(responsePacket)
            }

            val originTimestamp = ByteBuffer.wrap(request, 40, 8).long
            val receiveTimestamp = ByteBuffer.wrap(response, 40, 8).long
            val transmitTimestamp = ByteBuffer.wrap(response, 24, 8).long
            val clockOffset =
                ((receiveTimestamp - originTimestamp) + (transmitTimestamp - receiveTimestamp)) / 2
            timestamp = System.currentTimeMillis() + clockOffset
        }
        runBlocking { job.await() }
        val merchantTradeNb = randomString()

        val payload = hashMapOf(
            "env" to hashMapOf(
                "terminalType" to "APP"
            ),
            "merchantTradeNo" to merchantTradeNb,
            "orderAmount" to product.productPrice,
            "currency" to "BUSD",
            "goods" to hashMapOf(
                "goodsType" to "01",
                "goodsCategory" to "D000",
                "referenceGoodsId" to "7876763A3B",
                "goodsName" to product.productTitle,
                "goodsDetail" to product.productDesc
            )
        )

        val nonce = randomString()
        val payloadToSign = "$timestamp\n$nonce\n${Gson().toJson(payload)}\n"

        val signature = createHmacSignature(payloadToSign, merchantSecretKey).uppercase()
        val retrofitBuilder = RetrofitBuilder()

        val retrofitData = timestamp?.let {
            retrofitBuilder.binanceApi.createOrder(
                "application/json", it, nonce, merchantApiKey, signature, payload
            )
        }
        retrofitData?.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(
                call: Call<OrderResponse>, response: Response<OrderResponse>
            ) {
                prepayId = response.body()?.data?.prepayId ?: ""
                val binanceSDKTimestamp = System.currentTimeMillis()
                val nonceStr = randomString()
                val binanceSDKPayload =
                    "certSn=$merchantApiKey&merchantId=$merchantId&noncestr=$nonceStr&prepayId=$prepayId&timeStamp=$binanceSDKTimestamp"

                val signatureIntegration: String = createHmacSignature(
                    binanceSDKPayload, merchantSecretKey
                ).uppercase()

                val param = BinancePayParam(
                    merchantId.toString(),
                    prepayId,
                    binanceSDKTimestamp.toString(),
                    nonceStr,
                    merchantApiKey,
                    signatureIntegration
                )
                val binancePay = BinancePayFactory.getBinancePay(getApplication())
                binancePay.pay(param, listener)
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Toast.makeText(
                    getApplicationContext(), "API FAILURE RESPONSE", Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    fun randomString(): String {
        val random = SecureRandom()
        val bytes = ByteArray(16)
        random.nextBytes(bytes)
        return bytes.toHexString().take(32)
    }

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProduct(product)
        }
    }

    fun getProductId(userId: Int): LiveData<List<Product>> {
        myProductsLiveData = repository.getProductId(userId)
        return myProductsLiveData
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProduct(product)
        }
    }

    fun getProductsByCategory(productCategoryName: String): LiveData<List<Product>> {
        productLiveData = repository.getProductsByCategory(productCategoryName)
        return productLiveData
    }

    fun getProduct(): LiveData<List<Product>> {
        productLiveData = repository.getProducts()
        return productLiveData
    }
}
