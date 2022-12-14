package ru.netology.nerecipe

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.EditStage.Companion.textArg

class StageAdapter(
    private val context: Context,
    private var stageList: ArrayList<String>,
    private val navController: NavController,
    private val onEditStagesListener: OnEditStagesListener? = null
) :
    RecyclerView.Adapter<StageAdapter.StageViewHolder>() {

    /**
     * Creator function
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.stage_item, parent, false)

        //return ViewHolder
        return StageViewHolder(view)
    }

    /**
     * Binder function
     */
    override fun onBindViewHolder(holder: StageViewHolder, position: Int) {
        holder.bindData(stageList[position])
    }

    /**
     * Returns item counts
     * or list size
     */
    override fun getItemCount(): Int {
        return stageList.size
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {

                stageList[i] = stageList.set(i + 1, stageList[i])
            }
        } else {
            for (i in fromPosition downTo toPosition+1) {
                stageList[i] = stageList.set(i - 1, stageList[i])
            }
        }
        onEditStagesListener?.swapItems(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun editItem(position: Int) {
        val text = position.toString() + ARG_STRING_DELIMITER + stageList[position]
        removeItem(position)
        onEditStagesListener?.onEditStages(stageList)
        navController.navigate(
            R.id.action_editRecipe_to_editStage,
            Bundle().apply { textArg = text })
    }

    fun removeItem(position: Int) {
        stageList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        onEditStagesListener?.onEditStages(stageList)
    }

    class StageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var stageName: TextView = itemView.findViewById(R.id.stage_name_item)
        fun bindData(name: String) {
            stageName.text = name
        }

        fun paintSelected(selected: Boolean) {
            if (selected)
                stageName.setBackgroundColor(Color.LTGRAY)
            else
                stageName.setBackgroundColor(Color.WHITE)
        }
    }
}