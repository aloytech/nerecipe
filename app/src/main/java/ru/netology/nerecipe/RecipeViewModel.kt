package ru.netology.nerecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*


class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val empty = Recipe(
        id = 0,
        authorId = 0,
        name = "",
        categoryId = 0,
        likesCount = 0,
        servingLink = "",
        stages = emptyList(),
        stagesLink = emptyList()
    )
    //private val repository: RecipeRepository = RecipeRepositoryImpl(AppDb.getInstance(application).recipeDao())
    private val repository: RecipeRepository = RecipeRepositoryInMemoryImpl()

    val data = repository.getAll()
    var draft: String = ""
    private val edited = MutableLiveData(empty)

    fun likeDislike(id: Int, myId:Int) = repository.likeDislike(id,myId)
    fun removeById(id: Int) = repository.removeById(id)
    fun showRecipe(id: Int) = repository.showRecipe(id)
    fun getAuthorName(id: Int) = repository.getUserName(id)
    fun getCategoryName(id: Int) = repository.getCategoryName(id)
    fun likedByMe(id: Int, myId:Int) = repository.likedByMe(id,myId)
    fun save() {
        edited.value?.let {
            val dateFormatter = SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.getDefault())
            val date = dateFormatter.format(Date())
            val recipe = it.copy(stages = it.stages, stagesLink = it.stagesLink)
            repository.save(recipe)
        }
        edited.value = empty
        draft = ""
    }

    fun saveDraft(string: String) {
        draft = string
    }


    fun changeStages(stages: List<String>) {
        edited.value?.let {
            val newStages = stages
            if (it.stages == newStages) {
                return
            }
            edited.value = it.copy(stages = newStages)
        }
    }

    fun edit(id:Int) {
        edited.value = repository.showRecipe(id)
    }
}