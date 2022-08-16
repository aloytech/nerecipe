package ru.netology.nerecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


class RecipeRepositoryInMemoryImpl : RecipeRepository {

    private var users = emptyList<User>()
    private var categories = emptyList<Category>()
    private var nextId = 0
    private var currentUserId = EMPTY_STRING
    private var recipes = emptyList<Recipe>()
    private val data = MutableLiveData(recipes)

    init {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("FIREBASE", "success get ${document.data}")
                    val userJson = Gson().toJson(document.data)
                    val user = Gson().fromJson(userJson, User::class.java)
                    users = listOf(user) + users
                }
            }

        categories = listOf(
            Category(0, EUR),
            Category(1, ASIA),
            Category(2, PAN_ASIA),
            Category(3, EAST),
            Category(4, USA),
            Category(5, RUS),
            Category(6, MID)
        )

        db.collection("recipes")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG_FIREBASE, "success get ${document.data}")
                    val recipeJson = Gson().toJson(document.data)
                    val recipe = Gson().fromJson(recipeJson, Recipe::class.java)
                    if (recipe.id >= nextId) nextId = recipe.id + 1

                    recipes = listOf(recipe) + recipes
                    data.value = recipes
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG_FIREBASE, ERROR_SERVER, exception)
            }

        data.value = recipes
    }

    override fun getAll(): LiveData<List<Recipe>> = data

    override fun likeDislike(id: Int) {

        val db = Firebase.firestore
        val likedByMe = likedByMe(id)

        users = users.map {
            if (it.uid != currentUserId) {
                it
            } else {
                if (likedByMe) {
                    val user = it.copy(favorites = it.favorites?.minusElement(id))
                    db.collection("users").document(currentUserId).set(user)
                    user
                } else {
                    val newFavorites = if (it.favorites.isNullOrEmpty()) {
                        listOf(id)
                    } else {
                        listOf(id) + it.favorites
                    }
                    val user = it.copy(favorites = newFavorites)
                    db.collection("users").document(currentUserId).set(user)
                    user
                }
            }
        }

        recipes = recipes.map {
            if (it.id != id) {
                it
            } else {
                if (likedByMe) {
                    val recipe = it.copy(likesCount = it.likesCount - 1)
                    db.collection("recipes").document("$id").set(recipe)
                    recipe
                } else {
                    val recipe = it.copy(likesCount = it.likesCount + 1)
                    db.collection("recipes").document("$id").set(recipe)
                    recipe
                }
            }
        }
        data.value = recipes
    }


    override fun removeById(id: Int) {

        val db = Firebase.firestore
        db.collection("recipes").document("$id").delete()
        recipes = recipes.filter { it.id != id }
        users = users.map {
            if (it.iLikeIt(id)) {
                val user = it.copy(favorites = it.favorites?.minusElement(id))
                db.collection("users").document(user.uid).set(user)
                user
            } else {
                it
            }
        }

        data.value = recipes
    }

    override fun save(recipe: Recipe) {
        if (recipe.id == 0) {
            val newRecipe = recipe.copy(
                id = nextId++,
                authorId = currentUserId,
                name = recipe.name,
                stages = recipe.stages,
                stagesLink = recipe.stagesLink,
                servingLink = recipe.servingLink,
                categoryId = recipe.categoryId
            )
            recipes = listOf(newRecipe) + recipes
            data.value = recipes
            val db = Firebase.firestore
            db.collection("recipes").document("${newRecipe.id}").set(newRecipe)
            return
        }
        recipes = recipes.map {
            if (it.id != recipe.id) it else {
                val db = Firebase.firestore
                db.collection("recipes").document("${recipe.id}").set(recipe)
                it.copy(
                    name = recipe.name,
                    categoryId = recipe.categoryId,
                    stages = recipe.stages,
                    stagesLink = recipe.stagesLink,
                    servingLink = recipe.servingLink
                )

            }
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

    override fun likedByMe(id: Int): Boolean {
        return users.find {
            it.uid == currentUserId
        }!!.iLikeIt(id)
    }
}