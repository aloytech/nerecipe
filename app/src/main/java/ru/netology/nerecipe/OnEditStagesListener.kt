package ru.netology.nerecipe

interface OnEditStagesListener {
    fun onEditStages(stages:ArrayList<String>)
    fun swapItems(fromPosition: Int, toPosition: Int)
}