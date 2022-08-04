package ru.netology.nerecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nerecipe.databinding.RecipeShortBinding
import java.util.*

class RecipeAdapter(
    private val onInteractionListener: OnInteractionListener,
    private val getByKey: GetByKey

) : ListAdapter<Recipe, RecipeViewHolder>(RecipeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding, onInteractionListener, getByKey)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    private var unfilteredList = listOf<Recipe>()
    private var mFilterList = listOf<Recipe>()

    fun modifyList(list: List<Recipe>) {
        unfilteredList = list
        submitList(list)
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<Recipe>()

        if (!query.isNullOrEmpty()) {
            list.addAll(unfilteredList.filter {
                it.name.lowercase(Locale.getDefault()).contains(
                    query.toString()
                        .lowercase(Locale.getDefault())
                )
            })
        } else {
            list.addAll(unfilteredList)
        }
        submitList(list)
    }

    fun filterByCategory(selected: String) {
        val list = mutableListOf<Recipe>()

        if (selected.isNotEmpty()) {
            list.addAll(unfilteredList.filter {
                selected.contains(it.categoryId.toString())
            })
        } else {
            list.addAll(unfilteredList)
        }
        mFilterList = list
        submitList(list)
    }

    fun filterByFavorites() {
        val list = mutableListOf<Recipe>()
        if (mFilterList.isEmpty()) mFilterList = unfilteredList
        list.addAll(mFilterList.filter {
            getByKey.getLikedByMe(it.id, getCurrentUserId())
        })

        submitList(list)
    }
}

