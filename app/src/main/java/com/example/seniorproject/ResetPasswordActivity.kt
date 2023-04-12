package com.example.seniorproject

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.seniorproject.data.user.UserModel
import com.example.seniorproject.databinding.ActivityResetPasswordBinding
import java.security.MessageDigest
import javax.mail.*
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random


class ResetPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetPasswordBinding

    private var isValid: Boolean = false
    private val randomPassword = getRandPassword(8)
    private val userModel by viewModels<UserModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation)

        binding.forgetPasswordIcon.animation = animation
        binding.forgetPasswordTitle.animation = animation
        binding.forgetPasswordDescription.animation = animation
        binding.etForgetPasswordEmail.animation = animation
        binding.forgetPasswordNextBtn.animation = animation
        binding.forgetPasswordBackBtn.setOnClickListener {
            intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.forgetPasswordNextBtn.setOnClickListener {
            proceedToMakeSelection()
        }
        binding.sendMailBtn.setOnClickListener {
            sendEmail()
            intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
            Toast.makeText(this, "Email sent. Please check ur gmail", Toast.LENGTH_LONG).show()
            val sharedPreferences = getSharedPreferences("getUserName", MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId", 0)
            val hashedPassword = hashPassword(randomPassword)
            userModel.updatePassword(hashedPassword, userId)
            startActivity(intent)
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-512").digest(password.toByteArray())
        return bytes.fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun sendEmail() {
        try {
            val stringSenderEmail = "amineshadi10@gmail.com"
            val stringReceiverEmail = binding.mailDes.text.toString()
            val stringPasswordSenderEmail = "swfisdurglrzmmta"
            val sharedPreferences = getSharedPreferences("getUserName", MODE_PRIVATE)

            val userName = sharedPreferences.getString("userName", "user")
            val stringHost = "smtp.gmail.com"

            val properties = System.getProperties()

            properties["mail.smtp.host"] = stringHost
            properties["mail.smtp.port"] = "465"
            properties["mail.smtp.ssl.enable"] = "true"
            properties["mail.smtp.auth"] = "true"
            val session = Session.getInstance(properties, object : Authenticator() {
                //Authenticating the password
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
                }
            })
            val mimeMessage = MimeMessage(session)
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(stringReceiverEmail))
            mimeMessage.replyTo = InternetAddress.parse(stringSenderEmail, false)
            mimeMessage.subject = "Reset your password"

            mimeMessage.setText(
                "Dear $userName,\n\nsomeone, hopefully you- requested a password reset on this account." +
                        "if it wasn't you, you can safely ignore this email and your password will remain " +
                        "the same.\n\nUse $randomPassword as your password reset code\n\n " +
                        "(You can change it later on inside your app)"
            )


            val thread = Thread {
                try {
                    Transport.send(mimeMessage)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }

            thread.start()

        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun getRandPassword(n: Int): String {
        val characterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val random = Random(System.nanoTime())
        val password = StringBuilder()

        for (i in 0 until n) {
            val rIndex = random.nextInt(characterSet.length)
            password.append(characterSet[rIndex])
        }

        return password.toString()
    }

    private fun proceedToMakeSelection() {
        val userResetPasswordEmail = binding.etForgetPasswordEmail.text.toString()
        if (validateInputs()) {

            val users = userModel.getUserEmailAndPhoneNb(userResetPasswordEmail)

            users.observe(this) { user ->
                if (user.isEmpty()) {
                    binding.layoutForgetPasswordEmail.error = "Email registered not found"
                    binding.makeSelectionMailBox.visibility = View.INVISIBLE
                    return@observe
                }
                user.forEach {
                    val sharedPreferences = getSharedPreferences("getUserName", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.apply() {
                        editor.putString("userName", it.name)
                        editor.putInt("userId", it.userId)
                    }.apply()
                    if (userResetPasswordEmail == it.email) {
                        binding.makeSelectionMailBox.visibility = View.VISIBLE
                        binding.mailDes.text = userResetPasswordEmail
                    }

                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateInputs(): Boolean {
        isValid = true

        if (binding.etForgetPasswordEmail.text?.isEmpty() == true) {
            binding.layoutForgetPasswordEmail.error = "Please enter your email"
            isValid = false
        } else if (!isEmailValid(binding.etForgetPasswordEmail.text.toString())) {
            binding.layoutForgetPasswordEmail.error = "Please enter valid email"
            isValid = false
        } else {
            binding.layoutForgetPasswordEmail.isErrorEnabled = false
        }
        return isValid
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imessage: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imessage.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}