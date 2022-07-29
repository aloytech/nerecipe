package ru.netology.nerecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nerecipe.databinding.RecipeShortBinding

class RecipeAdapter(
    private val onInteractionListener: OnInteractionListener,
    private val getByKey: GetByKey

) : ListAdapter<Recipe, RecipeViewHolder>(RecipeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding, onInteractionListener,getByKey)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }
}