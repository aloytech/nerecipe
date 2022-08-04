package ru.netology.nerecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val empty = Recipe(
        id = 0,
        authorId = 0,
        name = EMPTY_STRING,
        categoryId = 0,
        likesCount = 0,
        servingLink =
        "https://firebasestorage.googleapis.com/v0/b/nerecipe-9498f.appspot.com/o/uploads%2Fd979ee2f-fd04-48e5-b452-a091f7d6a9ef?alt=media&token=5f3dbead-0bac-4728-89ad-99119842c3d1",
        stages = emptyList(),
        stagesLink = emptyList()
    )
    private val repository: RecipeRepository = RecipeRepositoryInMemoryImpl()

    val data = repository.getAll()
    var draft: String = EMPTY_STRING
    private val edited = MutableLiveData(empty)

    fun likeDislike(id: Int, myId: Int) = repository.likeDislike(id, myId)
    fun removeById(id: Int) = repository.removeById(id)
    fun showRecipe(id: Int): Recipe? {
        return if (id == 0) edited.value
        else repository.showRecipe(id)
    }

    fun getAuthorName(id: Int) = repository.getUserName(id)
    fun getCategoryName(id: Int) = repository.getCategoryName(id)
    fun likedByMe(id: Int, myId: Int) = repository.likedByMe(id, myId)
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
        draft = ""
    }

    fun saveDraft(string: String) {
        draft = string
    }

    fun addStage(stage: String) {
        edited.value?.let {
            edited.value = it.copy(stages = it.stages + listOf(stage))
        }
    }

    fun editServing(uri: String) {
        edited.value?.let {
            edited.value = it.copy(servingLink = uri)
        }
    }

    fun editCategory(categoryId: Int) {
        edited.value?.let {
            edited.value = it.copy(categoryId = categoryId)
        }
    }

    fun editName(name: String) {
        edited.value?.let {
            edited.value = it.copy(name = name)
        }
    }

    fun editStages(stages: List<String>) {
        edited.value?.let {
            if (it.stages == stages) {
                return
            }
            edited.value = it.copy(stages = stages)
        }
    }

    fun editStage(index: Int, content: String) {
        edited.value?.let {
            val newStages = it.stages.toMutableList()
            newStages[index] = content

            edited.value = it.copy(stages = newStages)
        }
    }

    fun edit(id: Int) {
        edited.value = if (id == 0) empty else repository.showRecipe(id)
    }

    fun editionCorrect(): EditCorrect {
        return EditCorrect.CORRECT
    }
}