package ru.netology.nerecipe

data class User(
    val uid: Int,
    val userName: String,
    val favorites: List<Int>? = null,
    val hash: Int
){
    override fun toString(): String {
        return userName
    }
}