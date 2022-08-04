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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import ru.netology.nerecipe.EditStage.Companion.textArg
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

        if (recipe != null) {
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
                categoryView.text = viewModel.getCategoryName(recipe.id)
                likeButton.text = recipe.likesToString()
                likeButton.isChecked = viewModel.likedByMe(id, getCurrentUserId())
                val manager = LinearLayoutManager(context)
                stagesListView.layoutManager = manager
                val itemAdapter = StageAdapter(this.root.context, ArrayList(recipe.stages), findNavController())
                stagesListView.adapter = itemAdapter
                stagesListView.addItemDecoration(
                    DividerItemDecoration(context,
                        DividerItemDecoration.VERTICAL)
                )

                likeButton.setOnClickListener {
                    viewModel.likeDislike(recipe.id, getCurrentUserId())
                }
                menuButton.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.menu_recipe)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.removeItem -> {
                                    viewModel.removeById(recipe.id)
                                    findNavController().navigate(R.id.action_showRecipeFull_to_feedFragment)
                                    true
                                }
                                R.id.editItem -> {
                                    viewModel.edit(id)
                                    findNavController().navigate(R.id.action_showRecipeFull_to_editRecipe,
                                        Bundle().apply
                                        {recipeIdArg = id  })
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }
        } else findNavController().navigateUp()

    }

}
