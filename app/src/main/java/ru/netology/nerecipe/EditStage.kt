package ru.netology.nerecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.FeedFragment.Companion.recipeIdArg
import ru.netology.nerecipe.databinding.EditStageBinding


class EditStage : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: EditStageBinding =
            EditStageBinding.inflate(inflater, container, false)

        binding.editStageName.requestFocus()
        if (viewModel.draft != "") {
            binding.editStageName.setText(viewModel.draft)
        }
        binding.saveButton.setOnClickListener {
            val content = binding.editStageName.text.toString()
            viewModel.addStage(content)
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        arguments?.textArg
            ?.let{
                binding.editStageName.setText(it)
            }


        binding.declineButton.setOnClickListener {
            binding.editStageName.text.clear()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.saveDraft(binding.editStageName.text.toString())
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        callback.isEnabled

        return binding.root
    }
}