package ru.netology.nerecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData



class RecipeRepositoryInMemoryImpl : RecipeRepository {
    private var users = listOf(
        User(
            0, "Alexey",listOf(1), "12345".hashCode()
        ),
        User(
            1, "Roman", hash = "54321".hashCode()
        ),
        User(
            2, "Ivan", hash = "111111".hashCode()
        )
    )

    private var categories = listOf(
        Category(0, "Европейская"),
        Category(1, "Азиатская"),
        Category(2, "Паназиатская"),
        Category(3, "Восточная"),
        Category(4, "Американская"),
        Category(5, "Русская"),
        Category(6, "Средиземноморская")
    )
    private var recipes = listOf(
        Recipe(
            id = 1,
            authorId = 1,
            name = "Блины",
            categoryId = 0,
            likesCount = 10,
            stages = listOf("смешать", "взбить", "замесить", "зажарить"),
            stagesLink = emptyList(),
            servingLink = "https://foma.ru/fotos/online/online%202013/maslenitsa2013/recepty/prostye_2.jpg"
        ),
        Recipe(
            id = 2,
            authorId = 2,
            name = "Блины2",
            categoryId = 1,
            likesCount = 15,
            stages = listOf("смешать", "взбить", "замесить", "зажарить"),
            stagesLink = emptyList(),
            servingLink = "https://foma.ru/fotos/online/online%202013/maslenitsa2013/recepty/prostye_2.jpg"
        )

    )

    private fun toRecipeView(recipes: List<Recipe>):List<RecipeView>{
        val listRecipeView = emptyList<RecipeView>()
        recipes.forEach {
            listRecipeView.plusElement(
                RecipeView(
                it.id,
                getUserName(it.id),
                    it.name,
                    getCategoryName(it.id),
                    it.likesToString(),
                    likedByMe(it.id, getCurrentUserId()),
                    it.servingLink,
                    it.stages,
                    it.stagesLink
            )
            )
        }
        return listRecipeView
    }
    private var nextId = 3
    private val data = MutableLiveData(recipes)
    override fun getAll(): LiveData<List<Recipe>> = data

    override fun likeDislike(id: Int, myId: Int) {

        val likedByMe = likedByMe(id,myId)
        users = users.map{
            if (it.uid != myId) {
                it
            } else {
                if (likedByMe) {
                    it.copy(favorites = it.favorites?.minusElement(id))
                } else {
                    it.copy(favorites = it.favorites?.plusElement(id))
                }
            }
        }
        recipes = recipes.map {
            if (it.id != id) {
                it
            } else {
                if (likedByMe) {
                    it.copy(likesCount = it.likesCount - 1)
                } else {
                    it.copy(likesCount = it.likesCount + 1)
                }
            }
        }
        data.value = recipes
    }


    override fun removeById(id: Int) {
        recipes = recipes.filter { it.id != id }
        data.value = recipes
    }

    override fun save(recipe: Recipe) {
        if (recipe.id == 0){

            recipes = listOf(recipe.copy(id = nextId++, authorId = 1)) + recipes
            data.value = recipes
            return
        }
        recipes = recipes.map {
            if (it.id != recipe.id) it else it.copy(stages = recipe.stages)
        }
        data.value = recipes

    }
    override fun showRecipe(id: Int): Recipe {
        return recipes.find { it.id == id }!!
    }

    override fun getCategoryName(id: Int): String {
        val categoryId = recipes.find { it.id == id }!!.categoryId
        return categories.find { it.cid == categoryId }!!.toString()
    }

    override fun getUserName(id: Int): String {
        val userId = recipes.find { it.id == id }!!.authorId
        return users.find { it.uid == userId }!!.toString()
    }

    override fun likedByMe(id: Int, myId: Int): Boolean {
        val favorites = users.find { it.uid == myId }!!.favorites
        if (favorites != null) {
            return favorites.contains(id)
        }
        return false
    }
}