package com.krypt0n.kara.UI.Helper

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.helper.ItemTouchHelper
import com.krypt0n.kara.UI.RecyclerViewAdapter

class RecyclerItemTouchHelper(
    dragDirs : Int, moveDirs : Int, val listener: RecyclerItemTouchHelperListener
) : ItemTouchHelper.SimpleCallback(dragDirs,moveDirs) {
    override fun onSwiped(viewHolder : ViewHolder, direction : Int) {
        if (true)
            listener.onSwiped(
                viewHolder,
                direction,
                viewHolder.adapterPosition
            )
    }
    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
        return true
    }
    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        val foregroundView = (viewHolder as RecyclerViewAdapter.CustomViewHolder).view_foreground
        getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        if (true) {
            val foregroundView = (viewHolder as RecyclerViewAdapter.CustomViewHolder).view_foreground
            getDefaultUIUtil().onSelected(foregroundView)
        }
    }
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView = (viewHolder as RecyclerViewAdapter.CustomViewHolder).view_foreground
        getDefaultUIUtil().onDraw(c,
            recyclerView,
            foregroundView,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView = (viewHolder as RecyclerViewAdapter.CustomViewHolder).view_foreground
        getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive)
    }
}