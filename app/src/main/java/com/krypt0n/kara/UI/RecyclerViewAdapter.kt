package com.krypt0n.kara.UI

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.krypt0n.kara.Note
import com.krypt0n.kara.NoteActivity
import com.krypt0n.kara.R
import com.krypt0n.kara.selected_item

class RecyclerViewAdapter(val mContext: Context?, val list: ArrayList<Note>) : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {
    override fun onBindViewHolder(holder : CustomViewHolder, position : Int) {
        holder.title_field.text = list[position].title
        holder.desc.text = list[position].text
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): CustomViewHolder {
        val v =  LayoutInflater.from(mContext).inflate(R.layout.notes_list,parent,false)

        return CustomViewHolder(v)
    }

    class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title_field = view.findViewById(R.id.note_title) as TextView
        val desc  = view.findViewById(R.id.note_desc) as TextView

        init {
            view.setOnClickListener {
                selected_item = adapterPosition
                view.context.startActivity(Intent(view.context, NoteActivity()::class.java))
            }
        }
    }
}