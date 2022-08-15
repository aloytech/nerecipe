package ru.netology.nerecipe

interface GetByKey {
    fun getAuthorName(recipeId: Int): String
    fun getLikedByMe(recipeId: Int): Boolean
    fun getCategory(recipeId: Int): String
}