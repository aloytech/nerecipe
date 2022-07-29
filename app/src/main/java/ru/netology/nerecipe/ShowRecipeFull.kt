package ru.netology.nerecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import ru.netology.nerecipe.FeedFragment.Companion.recipeIdArg
//import ru.netology.nerecipe.NewPostFragment.Companion.textArg
import ru.netology.nerecipe.databinding.RecipeFullBinding
import ru.netology.nerecipe.databinding.RecipeShortBinding
import java.lang.Exception

class ShowRecipeFull : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: RecipeFullBinding = RecipeFullBinding.inflate(inflater, container, false)

        val id = arguments?.recipeIdArg
        id?.let { init(binding, it) }
        viewModel.data.observe(viewLifecycleOwner) {
            id?.let { init(binding, it) }
        }
        return binding.root
    }

    private fun init(binding: RecipeFullBinding, id: Int) {
        val recipe = id.let { viewModel.showRecipe(it) }

        binding.apply {
            authorTextView.text = viewModel.getAuthorName(id)
            Picasso.get().load(recipe.servingLink)
                .into(servingView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        Log.i("RECIPE", "image load from url " + recipe.servingLink)
                    }

                    override fun onError(e: Exception?) {
                        servingView.setImageDrawable(R.mipmap.food.toDrawable())
                    }
                })
            recipeNameView.text = recipe.name
            likeButton.text = recipe.likesToString()
            likeButton.isChecked = false
            val listAdapter =
                ArrayAdapter(this.root.context, android.R.layout.simple_list_item_1, recipe.stages)
            stagesListView.adapter = listAdapter

            likeButton.setOnClickListener {
                viewModel.likeDislike(recipe.id, getCurrentUserId())
            }
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_main)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.removeItem -> {
                                true
                            }
                            R.id.editItem -> {
                                viewModel.edit(recipe.id)
                                val stages = recipe.stages
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