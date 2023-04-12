package com.example.seniorproject.interfaces

import android.util.Patterns
import android.widget.EditText
import com.example.seniorproject.databinding.ActivitySignUpBinding

interface UserSignupInputValidation {
    val bindingSignup: ActivitySignUpBinding
    var isValid: Boolean
    fun clearInputAfterSignUp(editText: ArrayList<EditText>)

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateInput(): Boolean {
        isValid = true
        if (bindingSignup.etNameSignup.text?.isEmpty() == true) {
            bindingSignup.layoutNameSignup.error = "Please Enter First Name"
            isValid = false
        } else {
            bindingSignup.layoutNameSignup.isErrorEnabled = false
        }
        if (bindingSignup.etEmailSignup.text?.isEmpty() == true) {
            bindingSignup.layoutEmailSignup.error = "Please enter your email"
            isValid = false
        } else if (!isEmailValid(bindingSignup.etEmailSignup.text.toString())) { // if the email structure is not valid
            bindingSignup.layoutEmailSignup.error = "Please enter valid email"
            isValid = false
        } else {
            bindingSignup.layoutEmailSignup.isErrorEnabled = false
        }
        if (bindingSignup.etMobileNumber.text?.isEmpty() == true) {
            bindingSignup.layoutMobileNumber.error = "Please Enter Your Mobile Number"
            isValid = false
        } else {
            bindingSignup.layoutMobileNumber.isErrorEnabled = false
        }

        if (bindingSignup.etPasswordSignup.text?.isNotEmpty() == true && bindingSignup.etPasswordSignup.text?.length!! < 5) { //        // checking minimum password Length
            bindingSignup.layoutPasswordSignup.error =
                "Password length must be more than 5 characters"
            isValid = false
        } else if (bindingSignup.etPasswordSignup.text?.isEmpty() == true) {
            bindingSignup.layoutPasswordSignup.error = "Please enter your password"
            isValid = false
        } else {
            bindingSignup.layoutPasswordSignup.isErrorEnabled = false
        }
        if (bindingSignup.etPasswordSignup.text.toString() != bindingSignup.etRepeatPasswordSignup.text.toString()) { // checking if the passwords match
            bindingSignup.layoutRepeatPasswordSignup.error = "Password does not match"
            isValid = false
        } else {
            bindingSignup.layoutRepeatPasswordSignup.isErrorEnabled = false
        }

        return isValid
    }

}