package com.krypt0n.kara.UI

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.krypt0n.kara.Note
import com.krypt0n.kara.R

class RecyclerViewAdapter(val mContext: Context?, val list: ArrayList<Note>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
        holder.title.setText(list.get(position).title)
        holder.desc.setText(list.get(position).text)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): MyViewHolder {
        var v =  LayoutInflater.from(mContext).inflate(R.layout.notes_list,parent,false)
        return MyViewHolder(v)
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById(R.id.note_title) as TextView
        val desc  = view.findViewById(R.id.note_desc) as TextView
    }
}