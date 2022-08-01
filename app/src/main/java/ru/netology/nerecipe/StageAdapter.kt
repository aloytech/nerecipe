package ru.netology.nerecipe

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StageAdapter(private val context: Context, var stageList : ArrayList<String>) :
    RecyclerView.Adapter<StageAdapter.StageViewHolder>() {

    /**
     * Creator function
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageViewHolder
    {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.stage_item, parent, false)

        //return ViewHolder
        return StageViewHolder(view)
    }

    /**
     * Binder function
     */
    override fun onBindViewHolder(holder: StageViewHolder, position: Int)
    {
        holder.bindData(stageList[position])
    }

    /**
     * Returns item counts
     * or list size
     */
    override fun getItemCount(): Int
    {
        return stageList.size
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {

                stageList[i] = stageList.set(i+1, stageList[i])
            }
        } else {
            for (i in fromPosition until toPosition) {
                stageList[i] = stageList.set(i-1, stageList[i])
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }
    fun removeItem(position: Int){
        stageList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,itemCount)
    }

    class StageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var stageName: TextView = itemView.findViewById(R.id.stage_name_item)
        fun bindData(name : String) {
            stageName.text = name
        }
        fun paintSelected(selected:Boolean){
            if (selected)
                stageName.setBackgroundColor(Color.LTGRAY)
            else
                stageName.setBackgroundColor(Color.WHITE)
        }
    }
}