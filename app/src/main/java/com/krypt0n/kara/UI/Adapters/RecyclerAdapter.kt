package com.krypt0n.kara.UI.Adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.krypt0n.kara.Activities.NoteActivity
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.*
import java.util.regex.Pattern

class RecyclerAdapter(private val list : ArrayList<Note>) : RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder>() {
    override fun onBindViewHolder(holder : CustomViewHolder, position : Int) {
        val title = list[position].title
        var text = list[position].text
        holder.titleField.text = title
        val pattern = Pattern.compile("(^([\\S]+)([ ])([\\S]+)([ ])([\\w\\d]+))")
        val matcher = pattern.matcher(text)
        text = if (matcher.find())
            matcher.group()
        else {
            if (text.length > 41) text.substring(0, 40).plus(" ...") else text
        }
        holder.descField.text = text
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): CustomViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notes_list,parent,false)
        return CustomViewHolder(v)
    }
    //remove note from list,will be called on swipe
    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
    }
    fun moveToTrash(position: Int){
        trash.add(notes[position])
        notes.removeAt(position)
        notifyItemRemoved(position)
    }
    /*restore note on swipe to right in trash section
    * this will run only on trash section,so can call list
    * and don't worry that it will use notes ArrayList*/
    fun restoreNote(position: Int){
        notes.add(trash[position])
        trash.removeAt(position)
        notifyItemRemoved(position)
    }
    //element look in list
    class CustomViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val titleField = view.findViewById(R.id.note_title) as TextView
        val descField = view.findViewById(R.id.note_desc) as TextView
        val viewForeground = view.findViewById(R.id.notes_view_foreground) as RelativeLayout

        init {
            updateImages()
            if (openedNotes){
                //set ability to click and popup note activity
                view.setOnClickListener {
                    selected_item = adapterPosition
                    view.context.startActivity(Intent(view.context, NoteActivity()::class.java))
                }
            }
        }
        //change background view icons
        private fun updateImages(){
            val right_icon = if (openedNotes)
                R.drawable.ic_delete
            else
                R.drawable.ic_delete_forever
            val left_icon = if (openedNotes)
                R.drawable.ic_delete_forever
            else
                R.drawable.ic_restore
            view.findViewById<ImageView>(R.id.swipe_left_icon).setImageResource(left_icon)
            view.findViewById<ImageView>(R.id.swipe_right_icon).setImageResource(right_icon)
        }
    }
}