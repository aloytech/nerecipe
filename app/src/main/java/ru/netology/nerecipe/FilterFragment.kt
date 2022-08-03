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
        "Европейская",
        "Азиатская",
        "Паназиатская",
        "Восточная",
        "Американская",
        "Русская",
        "Средиземноморская"
    )
    private val checkedItems = booleanArrayOf(true, true, true, true, true, true, true)



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var filterFlag =""
        arguments?.textArg?.let { filterFlag = it }
        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите категории")
                .setMultiChoiceItems(categories, checkedItems) { dialog, which, isChecked ->
                    checkedItems[which] = isChecked
                    //val name = categories[which] // Get the clicked item
                    //Toast.makeText(activity, name, Toast.LENGTH_LONG).show()
                }
                .setPositiveButton(
                    "Применить"
                ) { dialog, id ->

                    var s = "1$$"
                    if (filterFlag !="") s = "2$$"
                    for (i in checkedItems.indices) {
                        if(checkedItems[i]) {s += i.toString()}
                    }
                    if (s.length<4){
                        Toast.makeText(activity, "Выбор не может быть пустым", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_filterFragment_to_feedFragment, Bundle().apply { textArg= s+"0123456"})
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