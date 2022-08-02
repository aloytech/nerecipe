package ru.netology.nerecipe

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragManageAdapter(adapter: StageAdapter) : ItemTouchHelper.Callback()
{
    private var stageAdapter = adapter

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean
    {
        stageAdapter.swapItems(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.END){
            stageAdapter.removeItem(viewHolder.bindingAdapterPosition)
        } else if (direction == ItemTouchHelper.START){
            stageAdapter.editItem(viewHolder.bindingAdapterPosition)
        }
    }
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is StageAdapter.StageViewHolder) {
                val myViewHolder: StageAdapter.StageViewHolder =
                    viewHolder
                onItemSelected(myViewHolder)
            }
        }
    }
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }
    private fun onItemSelected(myViewHolder: StageAdapter.StageViewHolder) {
        myViewHolder.paintSelected(true)
    }

    private fun onItemReleased(myViewHolder: StageAdapter.StageViewHolder) {
        myViewHolder.paintSelected(false)

    }
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is StageAdapter.StageViewHolder) {
            val myViewHolder: StageAdapter.StageViewHolder =
                viewHolder
            onItemReleased(myViewHolder)
        }
    }

}