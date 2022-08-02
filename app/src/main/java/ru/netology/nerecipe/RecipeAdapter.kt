package ru.netology.nerecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nerecipe.databinding.RecipeShortBinding
import java.util.*

class RecipeAdapter(
    private val onInteractionListener: OnInteractionListener,
    private val getByKey: GetByKey

) : ListAdapter<Recipe, RecipeViewHolder>(RecipeDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding, onInteractionListener,getByKey)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }
    var unfilteredList = listOf<Recipe>()
    var mFilteredList: List<Recipe>? = null

    fun modifyList(list : List<Recipe>) {
        unfilteredList = list
        submitList(list)
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<Recipe>()

        // perform the data filtering
        if(!query.isNullOrEmpty()) {
            list.addAll(unfilteredList.filter {
                it.name.lowercase(Locale.getDefault()).contains(query.toString()
                    .lowercase(Locale.getDefault()))})
        } else {
            list.addAll(unfilteredList)
        }

        submitList(list)
    }

    }

