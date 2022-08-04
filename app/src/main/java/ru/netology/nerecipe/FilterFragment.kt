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
        var filterFlag = ""
        arguments?.textArg?.let { filterFlag = it }
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите категории")
                .setMultiChoiceItems(categories, checkedItems) { _, which, isChecked ->
                    checkedItems[which] = isChecked
                }
                .setPositiveButton(
                    "Применить"
                ) { _, _ ->

                    var s = "1$$"
                    if (filterFlag != "") s = "2$$"
                    for (i in checkedItems.indices) {
                        if (checkedItems[i]) {
                            s += i.toString()
                        }
                    }
                    if (s.length < 4) {
                        Toast.makeText(activity, "Выбор не может быть пустым", Toast.LENGTH_LONG)
                            .show()
                        findNavController().navigate(
                            R.id.action_filterFragment_to_feedFragment,
                            Bundle().apply { textArg = s + "0123456" })
                    } else {
                        findNavController().navigate(
                            R.id.action_filterFragment_to_feedFragment,
                            Bundle().apply { textArg = s })
                    }
                }
                .setNegativeButton("Отмена") { _, _ ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}