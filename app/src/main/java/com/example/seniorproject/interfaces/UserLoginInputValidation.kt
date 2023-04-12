package com.example.seniorproject.interfaces

import android.util.Patterns
import android.widget.EditText
import com.example.seniorproject.databinding.ActivityLoginBinding

interface UserLoginInputValidation {
    val bindingLogin: ActivityLoginBinding
    var isValid: Boolean
    fun clearInputAfterLogin(editText: ArrayList<EditText>)
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateInputs(): Boolean {
        isValid = true

        if (bindingLogin.etEmailLogin.text?.isEmpty() == true) {
            bindingLogin.layoutEmail.error = "Please enter your email"
            isValid = false
        } else if (!isEmailValid(bindingLogin.etEmailLogin.text.toString())) {
            bindingLogin.layoutEmail.error = "Please enter valid email"
            isValid = false
        } else {
            bindingLogin.layoutEmail.isErrorEnabled = false
        }
        // checking minimum password Length
        if (bindingLogin.etPasswordLogin.text?.isNotEmpty() == true && bindingLogin.etPasswordLogin.text?.length!! < 5) {
            bindingLogin.layoutPassword.error = "Password length must be more than 5 characters"
            isValid = false
        } else if (bindingLogin.etPasswordLogin.text?.isEmpty() == true) {
            bindingLogin.layoutPassword.error = "Please enter your password"
            isValid = false
        } else {
            bindingLogin.layoutPassword.isErrorEnabled = false
        }
        return isValid
    }
}
