package com.example.seniorproject

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import com.example.seniorproject.databinding.ActivityLoginBinding
import com.example.seniorproject.interfaces.UserLoginInputValidation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(), UserLoginInputValidation, LifecycleOwner {
    private lateinit var googleSignInClient: GoogleSignInClient
    override lateinit var bindingLogin: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val loginModel by viewModels<LoginModel>()
    override var isValid: Boolean = false

    companion object {
        const val RC_SIGN_IN = 9001
        const val EXTRA_NAME = "EXTRA NAME"
    }

    override fun clearInputAfterLogin(editText: ArrayList<EditText>) {
        for (et in editText) {
            et.text.clear()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.bindingLogin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogin.root)

        val sharedPreference: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
        val str = sharedPreference.getString("remember", "")
        if (str.equals("true")) {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        bindingLogin.tvForgetPassword.setOnClickListener {
            intent = Intent(applicationContext, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
        bindingLogin.plusIcon.setOnClickListener {
            goToSignUpPage()

        }
        bindingLogin.btLogin.setOnClickListener {
            performLogin()
        }
        bindingLogin.tvRegister.setOnClickListener {
            goToSignUpPage()
        }
        bindingLogin.checkboxRememberMe.setOnClickListener {
            if (bindingLogin.checkboxRememberMe.isChecked) {
                val preferences: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("remember", "true")
                editor.apply()

            } else if (!bindingLogin.checkboxRememberMe.isChecked) {
                val preferences: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("remember", "")
                editor.apply()
            }
        }
        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("823290302264-opjv41g25pj47ch6d4usto6o9uooou6l.apps.googleusercontent.com")
                .requestEmail().build()
        )

        bindingLogin.gmailLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        hideSystemBars()

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
//                        val user = auth.currentUser
//                        performDirectLogin(user)
//                        val userEmail = user?.email.toString()
                        Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Sign in failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Sign in failed!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToSignUpPage() {
        val preferences: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString("remember", "")
        editor.apply()
        startActivity(Intent(this, SignUpActivity::class.java))
        overridePendingTransition(R.anim.slide_right, R.anim.stay)
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-512").digest(password.toByteArray())
        return bytes.fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun performDirectLogin(user: FirebaseUser?) {
        val database = Firebase.database
        val sharedPreferences = getSharedPreferences("googleLogin", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val myRef = database.getReference("users")
        val userId = user!!.uid
        val userData = hashMapOf(
            "username" to user.displayName, "email" to user.email, "number" to user.phoneNumber
        )

        myRef.child(userId).setValue(userData).addOnSuccessListener {
            Toast.makeText(this, "User added to database successfully", Toast.LENGTH_LONG).show()
            editor.putString("userId", userId)
            editor.putString("userName", user.displayName)
            editor.putString("userEmail", user.email)
            editor.apply()
        }.addOnFailureListener {
            Toast.makeText(this, "Error adding user to database", Toast.LENGTH_LONG).show()
        }
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun performLogin() {
        val sharedPreferences = getSharedPreferences("usernameAndId", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if (validateInputs()) {

            val edittextArray = ArrayList<EditText>()
            edittextArray.addAll(
                listOf(
                    bindingLogin.etEmailLogin,
                    bindingLogin.etPasswordLogin,
                )
            )
            val email = bindingLogin.etEmailLogin.text.toString()
            val password = bindingLogin.etPasswordLogin.text.toString()
            val hashedPassword = hashPassword(password)
            val users = loginModel.findUserId()

            users.observe(this) { user ->
                user.forEach {
                    if (email == it.email && hashedPassword == it.password) {
                        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
                        editor.apply {
                            putInt("userId", it.userId)
                            putString("userEmail", it.email)
                            putString("userName", it.name)
                            apply()
                        }
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        clearInputAfterLogin(edittextArray)
                        return@observe
                    }
                }
                bindingLogin.layoutEmail.error = "Incorrect email or password,please try again."
                bindingLogin.tvForgetPassword.visibility = View.VISIBLE

            }

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
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
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