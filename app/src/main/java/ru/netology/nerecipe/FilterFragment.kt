package ru.netology.nerecipe

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.EditStage.Companion.textArg

class FilterFragment : DialogFragment() {

    private val categories = arrayOf(
        EUR,
        ASIA,
        PAN_ASIA,
        EAST,
        USA,
        RUS,
        MID
    )
    private var checkedItems = booleanArrayOf(true, true, true, true, true, true, true)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var filterString: String
        var filterFlag = EMPTY_STRING
        arguments?.textArg
            ?.let {
                filterFlag = it.substringBefore(ARG_STRING_DELIMITER)
                filterString = it.substringAfter(ARG_STRING_DELIMITER)
                if (filterString != EMPTY_STRING) {
                    for (i in checkedItems.indices) {
                        if (!filterString.contains(i.toString())) {
                            checkedItems[i] = false
                        }
                    }
                }
            }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(CHOOSE_CATEGORY)
                .setMultiChoiceItems(categories, checkedItems) { _, which, isChecked ->
                    checkedItems[which] = isChecked
                }
                .setPositiveButton(
                    APPLY
                ) { _, _ ->

                    var s = EMPTY_STRING

                    for (i in checkedItems.indices) {
                        if (checkedItems[i]) {
                            s += i.toString()
                        }
                    }

                    if (s == EMPTY_STRING || s == FULL_FILTER) {
                        if (s == EMPTY_STRING) Toast.makeText(
                            activity,
                            EMPTY_CHOICE,
                            Toast.LENGTH_LONG
                        ).show()
                        when (filterFlag) {
                            FILTER_FLAG_2, FILTER_FLAG_3 -> {
                                findNavController().navigate(
                                    R.id.action_filterFragment_to_feedFragment,
                                    Bundle().apply { textArg = FILTER_FLAG_2 + ARG_STRING_DELIMITER })
                            }
                            else -> {
                                findNavController().navigate(
                                    R.id.action_filterFragment_to_feedFragment,
                                    Bundle().apply { textArg = EMPTY_STRING })
                            }
                        }
                    } else {
                        when (filterFlag) {
                            EMPTY_STRING, FILTER_FLAG_1 -> {
                                findNavController().navigate(
                                    R.id.action_filterFragment_to_feedFragment,
                                    Bundle().apply {
                                        textArg = FILTER_FLAG_1 + ARG_STRING_DELIMITER + s
                                    })
                            }
                            FILTER_FLAG_2, FILTER_FLAG_3 -> {
                                findNavController().navigate(
                                    R.id.action_filterFragment_to_feedFragment,
                                    Bundle().apply {
                                        textArg = FILTER_FLAG_3 + ARG_STRING_DELIMITER + s
                                    })
                            }
                        }
                    }
                }
                .setNegativeButton(CANCEL) { _, _ ->
                    when (filterFlag) {
                        FILTER_FLAG_2, FILTER_FLAG_3 -> {
                            findNavController().navigate(
                                R.id.action_filterFragment_to_feedFragment,
                                Bundle().apply { textArg = FILTER_FLAG_2 + ARG_STRING_DELIMITER})
                        }
                        else -> {
                            findNavController().navigate(
                                R.id.action_filterFragment_to_feedFragment,
                                Bundle().apply { textArg = EMPTY_STRING + ARG_STRING_DELIMITER})
                        }
                    }
                }

            builder.create()
        } ?: throw IllegalStateException(EMPTY_ACTIVITY)
    }
}