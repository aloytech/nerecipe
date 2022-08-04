package ru.netology.nerecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.databinding.EditStageBinding


class EditStage : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private var editedStage: String = EMPTY_STRING

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

        arguments?.textArg
            ?.let {
                editedStage = it.substringBefore(ARG_STRING_DELIMITER)
                binding.editStageName.setText(it.substringAfter(ARG_STRING_DELIMITER))
            }

        binding.editStageName.requestFocus()
        if (viewModel.draft != EMPTY_STRING) {
            binding.editStageName.setText(viewModel.draft)
        }
        binding.saveButton.setOnClickListener {

            val content = binding.editStageName.text.toString()
            if (content != EMPTY_STRING) {

                viewModel.addStage(content)

                editedStage = EMPTY_STRING
                viewModel.saveDraft(EMPTY_STRING)
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()

            } else {
                Toast.makeText(activity, EMPTY_DESCRIPTION, Toast.LENGTH_LONG).show()
            }

        }

        binding.declineButton.setOnClickListener {
            findNavController().navigateUp()
            viewModel.saveDraft(EMPTY_STRING)
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