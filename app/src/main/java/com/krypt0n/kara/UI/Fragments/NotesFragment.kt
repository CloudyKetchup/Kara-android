package com.krypt0n.kara.UI.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.notes
import com.krypt0n.kara.UI.Adapters.RecyclerViewAdapter
import com.krypt0n.kara.UI.Helpers.RecyclerTouchHelper

class NotesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(
            R.layout.notes_fragment,
            container,
            false
        ) as View
        val adapter = RecyclerViewAdapter(notes,this)
        //recyclerview(shown list)
        val recyclerView = v.findViewById(R.id.notes_recycler_view) as RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
        }
        recyclerView.adapter = adapter
        //swipe to delete for list
        ItemTouchHelper(object : RecyclerTouchHelper(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT)
                    adapter.moveToTrash(viewHolder.adapterPosition)
                else if (direction == ItemTouchHelper.RIGHT)
                    adapter.removeItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(recyclerView)
        return v
    }
}