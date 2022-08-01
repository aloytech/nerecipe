package ru.netology.nerecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


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
    private val repository: RecipeRepository = RecipeRepositoryInMemoryImpl()

    val data = repository.getAll()
    var draft: String = ""
    private val edited = MutableLiveData(empty)

    fun likeDislike(id: Int, myId:Int) = repository.likeDislike(id,myId)
    fun removeById(id: Int) = repository.removeById(id)
    fun showRecipe(id: Int):Recipe{
        return if (id == 0) empty
        else repository.showRecipe(id)
    }
    fun getAuthorName(id: Int) = repository.getUserName(id)
    fun getCategoryName(id: Int) = repository.getCategoryName(id)
    fun likedByMe(id: Int, myId:Int) = repository.likedByMe(id,myId)
    fun save() {
        edited.value?.let {
            val recipe = it.copy(stages = it.stages, stagesLink = it.stagesLink)
            repository.save(recipe)
        }
        edited.value = empty
        draft = ""
    }

    fun saveDraft(string: String) {
        draft = string
    }
    fun addStage(stage:String){
        edited.value?.let{
            edited.value = it.copy(stages = it.stages + listOf(stage))
        }
    }

    fun editStage(stages: List<String>) {
        edited.value?.let {
            if (it.stages == stages) {
                return
            }
            edited.value = it.copy(stages = stages)
        }
    }

    fun edit(id:Int) {
        edited.value = repository.showRecipe(id)
    }
}