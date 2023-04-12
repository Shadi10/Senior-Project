package com.example.seniorproject.data.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private var userList: LiveData<List<User>> = MutableLiveData()

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)

    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun getUserEmailAndPhoneNb(userEmail: String): LiveData<List<User>> {
        userList = repository.getUserEmailAndPhoneNb(userEmail)
        return userList

    }

    fun updatePassword(password: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePassword(password, userId)
        }
    }

}
