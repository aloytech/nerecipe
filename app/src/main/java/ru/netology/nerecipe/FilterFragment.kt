package ru.netology.nerecipe

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.EditStage.Companion.textArg
import ru.netology.nerecipe.databinding.FragmentFilterBinding

class FilterFragment : DialogFragment() {

    private val categories = arrayOf(
        "Европейская",
        "Азиатская",
        "Паназиатская",
        "Восточная",
        "Американская",
        "Русская",
        "Средиземноморская"
    )
    private val checkedItems = booleanArrayOf(true, true, true, true, true, true, true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFilterBinding =
            FragmentFilterBinding.inflate(inflater, container, false)

        //with arrayadapter you have to pass a textview as a resource, and that is simple_list_item_1
        binding.filterListItem.adapter = this.context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_list_item_1,
                categories
            )
        }



        binding.filterListItem.setOnItemClickListener { adapterView,
                                                        view,
                                                        position,
                                                        l
            ->
            Toast.makeText(activity, categories[position], Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите категории")
                .setMultiChoiceItems(categories, checkedItems) { dialog, which, isChecked ->
                    checkedItems[which] = isChecked
                    val name = categories[which] // Get the clicked item
                    Toast.makeText(activity, name, Toast.LENGTH_LONG).show()
                }
                .setPositiveButton(
                    "Применить"
                ) { dialog, id ->
                    var s = "1$$"
                    for (i in checkedItems.indices) {
                        if(checkedItems[i]) {s += i.toString()}
                    }
                    if (s=="1$$"){
                        Toast.makeText(activity, "Выбор не может быть пустым", Toast.LENGTH_LONG).show()
                    }
                    else {
                        findNavController().navigate(R.id.action_filterFragment_to_feedFragment, Bundle().apply { textArg= s})
                    }
                    // User clicked OK, so save the selectedItems results somewhere
                    // or return them to the component that opened the dialog
                }
                .setNegativeButton("Отмена") { dialog, id ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}