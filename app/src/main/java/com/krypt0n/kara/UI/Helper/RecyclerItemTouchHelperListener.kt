package com.krypt0n.kara.UI.Helper

import android.support.v7.widget.RecyclerView

interface RecyclerItemTouchHelperListener {
    fun onSwiped(viewHolder: RecyclerView.ViewHolder,direction : Int,position : Int)
}
