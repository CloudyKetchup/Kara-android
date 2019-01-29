package com.krypt0n.kara.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krypt0n.kara.Note
import com.krypt0n.kara.R
import kotlin.collections.ArrayList

class NotesFragment() : Fragment() {
    lateinit var notes_list : ArrayList<Note>
    lateinit var recyclerView: RecyclerView

    @SuppressLint("ValidFragment")
    constructor(list : ArrayList<Note>) : this() {
        notes_list = list
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.notes_fragment, container, false) as View
        recyclerView = v.findViewById(R.id.notes_recycler_view) as RecyclerView
        val recyclerViewAdapter = RecyclerViewAdapter(context,notes_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = recyclerViewAdapter
        return v
    }
}
