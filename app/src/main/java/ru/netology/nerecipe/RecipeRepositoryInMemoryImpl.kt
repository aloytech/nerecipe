package ru.netology.nerecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


class RecipeRepositoryInMemoryImpl : RecipeRepository {

    private var users = emptyList<User>()
    private var categories = emptyList<Category>()
    private var nextId = 3
    private var recipes = emptyList<Recipe>()
    private val data = MutableLiveData(recipes)

    init {
        val db = Firebase.firestore

        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("FIREBASE","success get ${document.data}")
                    val userJson = Gson().toJson(document.data)
                    val user = Gson().fromJson(userJson,User::class.java)
                    users = listOf(user)+users
                }
            }

        categories = listOf(
            Category(0, "Европейская"),
            Category(1, "Азиатская"),
            Category(2, "Паназиатская"),
            Category(3, "Восточная"),
            Category(4, "Американская"),
            Category(5, "Русская"),
            Category(6, "Средиземноморская")
        )

        db.collection("recipes")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    Log.d("FIREBASE","success get ${document.data}")
                    val recipeJson = Gson().toJson(document.data)
                    val recipe = Gson().fromJson(recipeJson,Recipe::class.java)

                    recipes = listOf(recipe) + recipes
                    data.value = recipes
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE","error get",exception)
            }

        data.value = recipes
    }

    override fun getAll(): LiveData<List<Recipe>> = data

    override fun likeDislike(id: Int, myId: Int) {

        val likedByMe = likedByMe(id, myId)
        users = users.map {
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
        if (recipe.id == 0) {

            recipes = listOf(recipe.copy(id = nextId++, getCurrentUserId())) + recipes
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