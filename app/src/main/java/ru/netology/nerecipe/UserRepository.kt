package ru.netology.nerecipe

import androidx.lifecycle.LiveData

interface UserRepository {
    fun getAll(): LiveData<List<User>>
    fun likeDislike(uid: Int, recipeId: Int):Boolean
    fun getFavorites(uid:Int): List<Int>
    fun removeById(uid: Int)
    fun save(user: User)
    fun showUser(uid: Int):User
}