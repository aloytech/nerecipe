package ru.netology.nerecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
//import kotlinx.android.synthetic.main.fragment_feed.*
import ru.netology.nerecipe.databinding.FragmentFeedBinding
//import ru.netology.nmedia.NewPostFragment.Companion.textArg

class FeedFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.recipeIdArg: Int
            set(value) = putInt("RECIPE_ID", value)
            get() = getInt("RECIPE_ID")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFeedBinding = FragmentFeedBinding.inflate(inflater, container, false)

        val id = arguments?.recipeIdArg
        if (id != null) {
            viewModel.removeById(id)
        }

        val adapter = RecipeAdapter(object : OnInteractionListener {
            override fun onLikeListener(id: Int, likedByMe:Boolean) {
                viewModel.likeDislike(id,getCurrentUserId())
            }

            override fun onRemoveListener(id: Int) {
                viewModel.removeById(id)
            }

            override fun onEditItem(id:Int) {
                viewModel.edit(id)
                findNavController().navigate(R.id.action_feedFragment_to_editRecipe,
                Bundle().apply
                 {recipeIdArg = id  })
            }

            override fun onShowRecipe(id: Int) {
                findNavController().navigate(R.id.action_feedFragment_to_showRecipeFull,
                    Bundle().apply {
                        recipeIdArg = id
                    })
            }
        },object : GetByKey{
            override fun getAuthorName(recipeId: Int): String {
                return viewModel.getAuthorName(recipeId)
            }

            override fun getLikedByMe(recipeId: Int, myId: Int): Boolean {
                return viewModel.likedByMe(recipeId,myId)
            }

            override fun getCategory(recipeId: Int): String {
                return viewModel.getCategoryName(recipeId)
            }

        })

        binding.recipeRecycler.adapter = adapter
        binding.newRecipe.setOnClickListener {
            viewModel.edit(0)
            findNavController().navigate(R.id.action_feedFragment_to_editRecipe)

        }

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.modifyList(recipes)
        }
        binding.recipeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                (binding.recipeRecycler.adapter as RecipeAdapter).filter(newText)
                return true
            }
        })

        return binding.root
    }
}
