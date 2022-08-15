package ru.netology.nerecipe


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
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

        var filterString = EMPTY_STRING
        var filterFlag = EMPTY_STRING
        arguments?.textArg
            ?.let {
                filterFlag = it.substringBefore(ARG_STRING_DELIMITER)
                filterString = it.substringAfter(ARG_STRING_DELIMITER)
            }

        val adapter = RecipeAdapter(object : OnInteractionListener {
            override fun onLikeListener(id: Int, likedByMe: Boolean) {
                viewModel.likeDislike(id)
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

            override fun getLikedByMe(recipeId: Int): Boolean {
                return viewModel.likedByMe(recipeId)
            }

            override fun getCategory(recipeId: Int): String {
                return viewModel.getCategoryName(recipeId)
            }

        })


        binding.recipeRecycler.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.modifyList(recipes)
            when (filterFlag){
                FILTER_FLAG_3 -> {
                    binding.filterButton.setImageResource(R.drawable.ic_round_filter_list_24)
                    adapter.filterByCategory(filterString)
                    adapter.filterByFavorites()
                    binding.showFavoritesButton.isChecked = true
                }
                FILTER_FLAG_2 -> {
                    binding.filterButton.setImageResource(R.drawable.ic_round_filter_list_off_24)
                    adapter.filterByFavorites()
                    binding.showFavoritesButton.isChecked = true
                }
                FILTER_FLAG_1 -> {
                    binding.filterButton.setImageResource(R.drawable.ic_round_filter_list_24)
                    adapter.filterByCategory(filterString)
                    binding.showFavoritesButton.isChecked = false
                }
                else -> {
                    binding.showFavoritesButton.isChecked = false
                    binding.filterButton.setImageResource(R.drawable.ic_round_filter_list_off_24)
                }
            }
        }

        binding.filterButton.setOnClickListener {
            when (filterFlag){
                EMPTY_STRING -> findNavController().navigate(R.id.action_feedFragment_to_filterFragment)
                else ->{
                    findNavController().navigate(
                        R.id.action_feedFragment_to_filterFragment,
                        Bundle().apply { textArg = filterFlag + ARG_STRING_DELIMITER + filterString })
                }
            }
        }

        binding.showFavoritesButton.setOnClickListener {
            when (filterFlag){
                EMPTY_STRING -> {
                    adapter.filterByFavorites()
                    filterFlag = FILTER_FLAG_2
                }
                FILTER_FLAG_3 ->{
                    filterFlag = FILTER_FLAG_1
                    adapter.filter(null)
                    adapter.filterByCategory(filterString)
                }
                FILTER_FLAG_1 ->{
                    filterFlag = FILTER_FLAG_3
                    adapter.filterByCategory(filterString)
                    adapter.filterByFavorites()
                }
                FILTER_FLAG_2 -> {
                    filterFlag = EMPTY_STRING
                    adapter.filter(null)
                }
            }
            if (binding.showFavoritesButton.isChecked) {
                if (filterFlag != EMPTY_STRING) adapter.filterByCategory(filterString)
                adapter.filterByFavorites()
            } else {
                adapter.filter(null)
                if (filterFlag != FILTER_FLAG_2) {
                    adapter.filterByCategory(filterString)
                    filterFlag !=FILTER_FLAG_1
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

        binding.recipeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                adapter.filter(newText)
                return true
            }
        })

        return binding.root
    }
}
