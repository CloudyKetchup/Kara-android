package com.krypt0n.kara.UI.Adapters

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.krypt0n.kara.*
import com.krypt0n.kara.Repository.Note
import com.krypt0n.kara.Repository.notes
import com.krypt0n.kara.Repository.selected_item
import com.krypt0n.kara.Repository.trash
import com.krypt0n.kara.UI.Fragments.NotesFragment
import com.krypt0n.kara.UI.Fragments.TrashFragment
import java.util.regex.Pattern

class RecyclerViewAdapter(val list: ArrayList<Note>,val fragment : Fragment) : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {
    override fun onBindViewHolder(holder : CustomViewHolder, position : Int) {
        val title = list[position].title
        var text = list[position].text
        holder.title_field.text = title
        val pattern = Pattern.compile("(^([\\S]+)([ ])([\\S]+)([ ])([\\w\\d]+))")
        val matcher = pattern.matcher(text)
        text = if (matcher.find())
            matcher.group()
        else {
            if (text.length > 41) text.substring(0, 40).plus(" ...") else text
        }
        holder.desc.text = text
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): CustomViewHolder {
        val v =  LayoutInflater.from(parent.context).inflate(R.layout.notes_list,parent,false)
        return CustomViewHolder(v,fragment)
    }
    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
    }
    fun moveToTrash(position: Int){
        trash.add(list[position])
        list.removeAt(position)
        notifyItemRemoved(position)
    }
    fun restoreItem(note: Note, position: Int){
        list.add(position,note)
        notifyItemInserted(position)
    }
    class CustomViewHolder(view : View,fragment: Fragment) : RecyclerView.ViewHolder(view){
        val title_field= view.findViewById(R.id.note_title) as TextView
        val desc= view.findViewById(R.id.note_desc) as TextView
        val view_foreground= view.findViewById(R.id.view_foreground) as RelativeLayout
        init {
            when (fragment.tag){
                NotesFragment().tag -> updateIcons(view,
                    R.drawable.ic_delete_forever,
                    R.drawable.ic_delete)
                TrashFragment().tag -> updateIcons(view,
                    R.drawable.ic_restore,
                    R.drawable.ic_delete_forever)
            }
            view.setOnClickListener {
                selected_item = adapterPosition
                view.context.startActivity(Intent(view.context, NoteActivity()::class.java))
            }
        }
        fun updateIcons(view : View,src_left : Int,src_right : Int){
           view.findViewById<ImageView>(R.id.swipe_delete_forever_icon).setImageResource(src_left)
           view.findViewById<ImageView>(R.id.swipe_delete_icon).setImageResource(src_right)
        }
    }
}