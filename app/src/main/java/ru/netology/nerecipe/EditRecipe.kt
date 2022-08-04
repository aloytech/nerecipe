package ru.netology.nerecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import ru.netology.nerecipe.databinding.RecipeEditBinding
import java.lang.Exception

class EditRecipe : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private lateinit var stagesArray: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: RecipeEditBinding = RecipeEditBinding.inflate(inflater, container, false)

        init(binding)

        return binding.root
    }

    private fun init(binding: RecipeEditBinding) {
        val recipe = viewModel.showRecipe(0)
        if (recipe != null) {

            binding.apply {

                if (recipe.servingLink != "") {
                    Picasso.get().load(recipe.servingLink)
                        .into(servingView, object : com.squareup.picasso.Callback {
                            override fun onSuccess() {
                                Log.i("RECIPE", "image load from url " + recipe.servingLink)
                            }

                            override fun onError(e: Exception?) {
                                servingView.setImageDrawable(R.mipmap.food.toDrawable())
                            }
                        })
                } else servingView.setImageDrawable(R.mipmap.food.toDrawable())

                servingView.setOnClickListener {
                    findNavController().navigate(R.id.action_editRecipe_to_galleryFragment)
                }

                recipeNameEdit.setText(recipe.name)
                categoryButton.text =
                    if (recipe.id != 0) viewModel.getCategoryName(recipe.id) else "Европейская"
                categoryButton.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.menu_categories)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.european -> {
                                    categoryButton.text = "Европейская"
                                    viewModel.editCategory(0)
                                    true
                                }
                                R.id.asian -> {
                                    categoryButton.text = "Азиатская"
                                    viewModel.editCategory(1)
                                    true
                                }
                                R.id.panasian -> {
                                    categoryButton.text = "Паназиатская"
                                    viewModel.editCategory(2)
                                    true
                                }
                                R.id.eastern -> {
                                    categoryButton.text = "Восточная"
                                    viewModel.editCategory(3)
                                    true
                                }
                                R.id.american -> {
                                    categoryButton.text = "Американская"
                                    viewModel.editCategory(4)
                                    true
                                }
                                R.id.russian -> {
                                    categoryButton.text = "Русская"
                                    viewModel.editCategory(5)
                                    true
                                }
                                R.id.mediterranean -> {
                                    categoryButton.text = "Средиземноморская"
                                    viewModel.editCategory(6)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }

                val manager = LinearLayoutManager(context)
                stages.layoutManager = manager
                stagesArray = ArrayList(recipe.stages)
                val itemAdapter = StageAdapter(this.root.context, stagesArray, findNavController())

                stages.adapter = itemAdapter
                stages.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )

                val callback =
                    DragManageAdapter(
                        itemAdapter
                    )

                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(stages)
                addStageButton.setOnClickListener {
                    findNavController().navigate(R.id.action_editRecipe_to_editStage)
                }
                saveButton.setOnClickListener {
                    viewModel.editName(recipeNameEdit.text.toString())

                    if (viewModel.editionCorrect() == EditCorrect.CORRECT) {
                        viewModel.save()
                        findNavController().navigateUp()
                    } else Toast.makeText(
                        this.root.context,
                        viewModel.editionCorrect().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else findNavController().navigateUp()
    }
}