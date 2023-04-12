package com.example.seniorproject.data.user

import androidx.lifecycle.LiveData


class UserRepository(private val userDao: UserDao) {
    fun addUser(user: User) {
        userDao.addUser(user)
    }

    fun findUserId(): LiveData<List<User>> {
        return userDao.getUsers()
    }

    fun getUserEmailAndPhoneNb(userEmail: String): LiveData<List<User>> {
        return userDao.getUserEmailAndPhoneNb(userEmail)
    }

    fun updatePassword(password: String, userId: Int) {
        userDao.updatePassword(password, userId)
    }
}