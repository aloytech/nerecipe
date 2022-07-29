package ru.netology.nerecipe

import androidx.lifecycle.LiveData

interface RecipeRepository {
    fun getAll(): LiveData<List<Recipe>>
    fun likeDislike(id: Int, myId:Int)
    fun removeById(id: Int)
    fun save(recipe: Recipe)
    fun showRecipe(id: Int):Recipe
    fun getCategoryName(id:Int):String
    fun getUserName(id:Int):String
    fun likedByMe(id:Int, myId: Int):Boolean
}