package ru.netology.nerecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.PopupMenu
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import ru.netology.nerecipe.FeedFragment.Companion.recipeIdArg
import ru.netology.nerecipe.databinding.RecipeEditBinding
//import ru.netology.nerecipe.NewPostFragment.Companion.textArg
import ru.netology.nerecipe.databinding.RecipeFullBinding
import ru.netology.nerecipe.databinding.RecipeShortBinding
import java.lang.Exception

class EditRecipe : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private var currentCategory = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: RecipeEditBinding = RecipeEditBinding.inflate(inflater, container, false)

        val id = arguments?.recipeIdArg
        id?.let { init(binding, it) }
        viewModel.data.observe(viewLifecycleOwner) {
            id?.let { init(binding, it) }
        }

        return binding.root
    }

    private fun init(binding: RecipeEditBinding, id: Int) {
        val recipe = id.let { viewModel.showRecipe(it) }

        binding.apply {

            servingView.setImageDrawable(R.mipmap.food.toDrawable())

            recipeNameEdit.setText(recipe.name)
            categoryButton.text = viewModel.getCategoryName(recipe.id)
            categoryButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_categories)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.european -> {
                                categoryButton.text = "Европейская"
                                currentCategory = 0
                                true
                            }
                            R.id.asian -> {
                                categoryButton.text = "Азиатская"
                                currentCategory = 1
                                true
                            }
                            R.id.panasian -> {
                                categoryButton.text = "Паназиатская"
                                currentCategory = 2
                                true
                            }
                            R.id.eastern -> {
                                categoryButton.text = "Восточная"
                                currentCategory = 3
                                true
                            }
                            R.id.american -> {
                                categoryButton.text = "Американская"
                                currentCategory = 4
                                true
                            }
                            R.id.russian -> {
                                categoryButton.text = "Русская"
                                currentCategory = 5
                                true
                            }
                            R.id.mediterranean -> {
                                categoryButton.text = "Средиземноморская"
                                currentCategory = 6
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