package com.example.seniorproject.data.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun addUser(users: User)

    @Query("Select * from user_table")
    fun getUsers(): LiveData<List<User>>

    @Update
    fun updateUser(users: User)

    @Delete
    fun deleteUser(users: User)

    @Query("Select * from user_table where email=:userEmail")
    fun getUserEmailAndPhoneNb(userEmail: String): LiveData<List<User>>

    @Query("UPDATE user_table SET password=:password WHERE userId =:userId")
    fun updatePassword(password: String, userId: Int)

}