package ru.netology.nerecipe

data class Category(
    val cid: Int,
    val categoryName: String
) {
    override fun toString(): String {
        return categoryName
    }
}