package ru.netology.nerecipe

data class RecipeView(
    val id: Int,
    val authorName: String,
    val name: String,
    val categoryName: String,
    val likes: String,
    val likedByMe:Boolean,
    val servingLink: String,
    val stages: List<String>,
    val stagesLink: List<String>
)