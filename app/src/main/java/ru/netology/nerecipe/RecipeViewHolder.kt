package ru.netology.nerecipe

import android.util.Log
import android.widget.PopupMenu
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.netology.nerecipe.databinding.RecipeShortBinding
import java.lang.Exception

class RecipeViewHolder(
    private val binding: RecipeShortBinding,
    private val onInteractionListener: OnInteractionListener,
    private val getByKey: GetByKey
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.apply {
            authorTextView.text = getByKey.getAuthorName(recipe.id)
            Picasso.get().load(recipe.servingLink)
                .into(servingView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        Log.i("RECIPE", "image load from url " + recipe.servingLink)
                    }

                    override fun onError(e: Exception?) {
                        servingView.setImageDrawable(R.mipmap.food.toDrawable())
                    }
                })
            clickOnRecipe.setOnClickListener {
                onInteractionListener.onShowRecipe(recipe.id)
            }
            recipeNameView.text = recipe.name
            categoryView.text = getByKey.getCategory(recipe.id)
            likeButton.text = recipe.likesToString()
            likeButton.isChecked = getByKey.getLikedByMe(recipe.id, getCurrentUserId())

            likeButton.setOnClickListener {
                onInteractionListener.onLikeListener(recipe.id,likeButton.isChecked)
            }
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_recipe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.removeItem -> {
                                onInteractionListener.onRemoveListener(recipe.id)
                                true
                            }
                            R.id.editItem -> {
                                onInteractionListener.onEditItem(recipe.id)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}