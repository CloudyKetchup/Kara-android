package com.krypt0n.kara.UI

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.krypt0n.kara.R

class MyAdapter (list : List<ListItem>, context : Context) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    var list_items : List<ListItem> = list
    var context : Context = context


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        var v : View? = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_list,parent,false)
        return ViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, postition: Int) {
        var listItem : ListItem = list_items.get(postition)
        holder.text_view_head.text = listItem.head
        holder.text_view_desc.text = listItem.description
    }

    override fun getItemCount(): Int {
        return 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text_view_head : TextView = itemView.findViewById(R.id.note_title)
        var text_view_desc : TextView = itemView.findViewById(R.id.note_desc)


    }
}