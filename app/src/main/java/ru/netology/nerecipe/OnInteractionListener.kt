package ru.netology.nerecipe

interface OnInteractionListener {
    fun onLikeListener(id: Int, likedByMe: Boolean)
    fun onRemoveListener(id: Int)
    fun onEditItem(id: Int)
    fun onShowRecipe(id: Int)
}