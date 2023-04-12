package com.example.seniorproject

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.seniorproject.data.user.User
import com.example.seniorproject.data.user.UserModel
import com.example.seniorproject.databinding.ActivitySignUpBinding
import com.example.seniorproject.interfaces.UserSignupInputValidation
import java.security.MessageDigest


class SignUpActivity : AppCompatActivity(), UserSignupInputValidation {

    override lateinit var bindingSignup: ActivitySignUpBinding
    override var isValid: Boolean = false
    private val userModel by viewModels<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSignup = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(bindingSignup.root)
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
        bindingSignup.cardViewSignup.animation = animation
        hideSystemBars()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        bindingSignup.backIcon.setOnClickListener {
            goToLoginPage()
        }
        bindingSignup.btSignUp.setOnClickListener {
            performSignUp()
        }

        bindingSignup.countryCodePicker.selectedCountryName
        bindingSignup.countryCodePicker.selectedCountryCode
    }


    private fun goToLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_left, R.anim.stay)
    }


    private fun performSignUp() {
        val edittextArray = ArrayList<EditText>()
        edittextArray.addAll(
            listOf(
                bindingSignup.etEmailSignup,
                bindingSignup.etNameSignup,
                bindingSignup.etPasswordSignup,
                bindingSignup.etMobileNumber,
                bindingSignup.etRepeatPasswordSignup
            )
        )
        if (validateInput()) {
            val name = bindingSignup.etNameSignup.text.toString()
            val email = bindingSignup.etEmailSignup.text.toString()
            val countryCode = bindingSignup.countryCodePicker.selectedCountryCode.toString()
            val mobileNumber = (countryCode + " " + bindingSignup.etMobileNumber.text)
            val password = bindingSignup.etPasswordSignup.text.toString()
            val hashedPassword = hashPassword(password)
            val user = User(0, name, email, mobileNumber, hashedPassword)

            clearInputAfterSignUp(edittextArray)
            userModel.addUser(user)
            Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)

        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-512").digest(password.toByteArray())
        return bytes.fold("") { str, it -> str + "%02x".format(it) }
    }

    override fun clearInputAfterSignUp(editText: ArrayList<EditText>) {
        for (et in editText) {
            et.text.clear()
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}