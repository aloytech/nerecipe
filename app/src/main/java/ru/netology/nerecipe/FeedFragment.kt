package ru.netology.nerecipe


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import ru.netology.nerecipe.EditStage.Companion.textArg
import ru.netology.nerecipe.databinding.FragmentFeedBinding

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

        arguments?.recipeIdArg?.let { viewModel.removeById(it) }

        var filterString = ""
        var filterFlag = ""
        arguments?.textArg
            ?.let {
                filterFlag = it.substringBefore("$$")
                filterString = it.substringAfter("$$")
            }

        val adapter = RecipeAdapter(object : OnInteractionListener {
            override fun onLikeListener(id: Int, likedByMe: Boolean) {
                viewModel.likeDislike(id, getCurrentUserId())
            }

            override fun onRemoveListener(id: Int) {
                viewModel.removeById(id)
            }

            override fun onEditItem(id: Int) {
                viewModel.edit(id)
                findNavController().navigate(R.id.action_feedFragment_to_editRecipe,
                    Bundle().apply
                    { recipeIdArg = id })
            }

            override fun onShowRecipe(id: Int) {
                findNavController().navigate(R.id.action_feedFragment_to_showRecipeFull,
                    Bundle().apply {
                        recipeIdArg = id
                    })
            }
        }, object : GetByKey {
            override fun getAuthorName(recipeId: Int): String {
                return viewModel.getAuthorName(recipeId)
            }

            override fun getLikedByMe(recipeId: Int, myId: Int): Boolean {
                return viewModel.likedByMe(recipeId, myId)
            }

            override fun getCategory(recipeId: Int): String {
                return viewModel.getCategoryName(recipeId)
            }

        })


        binding.recipeRecycler.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.modifyList(recipes)
            if (filterFlag != "") {
                adapter.filterByCategory(filterString)
                if (filterFlag == "2") {
                    adapter.filterByFavorites()
                    binding.showFavoritesButton.isChecked = true
                }
            }
        }


        binding.recipeRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.newRecipe.setOnClickListener {
            viewModel.edit(0)
            findNavController().navigate(R.id.action_feedFragment_to_editRecipe)

        }
        binding.filterButton.isChecked = filterFlag != "" && filterString != "0123456"
        binding.filterButton.setOnClickListener {
            if (!binding.showFavoritesButton.isChecked) findNavController().navigate(R.id.action_feedFragment_to_filterFragment)
            else findNavController().navigate(
                R.id.action_feedFragment_to_filterFragment,
                Bundle().apply { textArg = "2" })
        }

        binding.recipeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                adapter.filter(newText)
                return true
            }
        })
        binding.showFavoritesButton.setOnClickListener {
            if (binding.showFavoritesButton.isChecked) {
                if (filterFlag != "") adapter.filterByCategory(filterString)
                adapter.filterByFavorites()
            } else {
                adapter.filter(null)
                if (filterFlag != "") adapter.filterByCategory(filterString)
            }
        }

        return binding.root
    }
}
