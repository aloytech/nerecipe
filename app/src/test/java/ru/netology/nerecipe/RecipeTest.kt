package ru.netology.nerecipe

import org.junit.Assert.*

import org.junit.Test

class RecipeTest {

    @Test
    fun likesToStringK() {
        val expected = "1,2K"
        val recipe = Recipe(
            id = 1,
            authorId = 0,
            name = "Блины",
            categoryId = 0,
            likesCount = 1260,
            servingLink = "http://",
            stages = listOf("Смешать","Замесит","Испечь"),
            stagesLink = listOf("http://")
        )
        val actual = recipe.likesToString()
        assertEquals(expected,actual)
    }

    @Test
    fun likesToStringKAbove10() {
        val expected = "20K"
        val recipe = Recipe(
            id = 1,
            authorId = 0,
            name = "Блины",
            categoryId = 0,
            likesCount = 20260,
            servingLink = "http://",
            stages = listOf("Смешать","Замесит","Испечь"),
            stagesLink = listOf("http://")
        )
        val actual = recipe.likesToString()
        assertEquals(expected,actual)
    }

    @Test
    fun likesToStringM() {
        val expected = "2,3M"
        val recipe = Recipe(
            id = 1,
            authorId = 0,
            name = "Блины",
            categoryId = 0,
            likesCount =2360200,
            servingLink = "http://",
            stages = listOf("Смешать","Замесит","Испечь"),
            stagesLink = listOf("http://")
        )
        val actual = recipe.likesToString()
        assertEquals(expected,actual)
    }
    @Test
    fun likesToStringUnder1000() {
        val expected = "202"
        val recipe = Recipe(
            id = 1,
            authorId = 0,
            name = "Блины",
            categoryId = 0,
            likesCount = 202,
            servingLink = "http://",
            stages = listOf("Смешать","Замесит","Испечь"),
            stagesLink = listOf("http://")
        )
        val actual = recipe.likesToString()
        assertEquals(expected,actual)
    }

}