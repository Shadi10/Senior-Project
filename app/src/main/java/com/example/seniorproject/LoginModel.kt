package com.example.seniorproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.data.user.User
import com.example.seniorproject.data.user.UserDatabase
import com.example.seniorproject.data.user.UserRepository

class LoginModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
     var userList: LiveData<List<User>> = MutableLiveData()

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun findUserId(): LiveData<List<User>> {
        userList = repository.findUserId()
        return userList
    }
}