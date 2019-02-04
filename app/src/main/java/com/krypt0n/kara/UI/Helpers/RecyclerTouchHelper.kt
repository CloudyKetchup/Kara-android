package com.krypt0n.kara.UI.Helpers

import android.graphics.Canvas
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.krypt0n.kara.UI.Adapters.RecyclerViewAdapter
import com.krypt0n.kara.UI.Fragments.NotesFragment
import com.krypt0n.kara.UI.Fragments.TrashFragment

open class RecyclerTouchHelper()
    : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
    //not used but must be implemented
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }
    //action done when swipe note to left,most important part
    override fun onSwiped(viewHolder : RecyclerView.ViewHolder, direction : Int) {
        //need to be override in fragment that will use this class
    }
    //remove note from list(recyclerview)
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as RecyclerViewAdapter.CustomViewHolder).view_foreground
        getDefaultUIUtil().clearView(foregroundView)
    }
    //background of list item(red color,delete icon)
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView = (viewHolder as RecyclerViewAdapter.CustomViewHolder).view_foreground
        getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive)
    }
    //item that appear above background
    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView = (viewHolder as RecyclerViewAdapter.CustomViewHolder).view_foreground
        getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive)
    }
}