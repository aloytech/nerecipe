package ru.netology.nerecipe

data class User(
    val uid: String,
    val userName: String,
    val favorites: List<Int>? = null,
    val hash: Int
){
    override fun toString(): String {
        return userName
    }
    fun iLikeIt(id:Int):Boolean{
        favorites?.let{
            if (it.contains(id)) return true
        }
        return false
    }
}