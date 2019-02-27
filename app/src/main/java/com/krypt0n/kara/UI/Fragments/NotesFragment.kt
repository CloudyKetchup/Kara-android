package com.krypt0n.kara.UI.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.*
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.notes
import com.krypt0n.kara.Repository.openedNotes
import com.krypt0n.kara.UI.Adapters.RecyclerAdapter
import com.krypt0n.kara.UI.Helpers.RecyclerTouchHelper

class NotesFragment : Fragment() {
    init {
        openedNotes = true
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(
            R.layout.notes_fragment,
            container,
            false
        ) as View
        val adapter = RecyclerAdapter(notes)
        //recyclerView(shown list)
        val recyclerView = v.findViewById(R.id.notes_recycler_view) as RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
        }
        recyclerView.adapter = adapter
        //swipe to delete option for list
        ItemTouchHelper(object : RecyclerTouchHelper() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT)
                    adapter.removeItem(viewHolder.adapterPosition)
                else if (direction == ItemTouchHelper.LEFT)
                    adapter.moveToTrash(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(recyclerView)
        return v
    }
}