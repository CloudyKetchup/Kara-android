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
import com.krypt0n.kara.NoteActivity
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.*
import java.io.FileOutputStream
import java.util.regex.Pattern

class RecyclerAdapter(val list : ArrayList<Note>, val fragment : Fragment) : RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder>() {
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
        holder.desc_field.text = text
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
    * and don't worry that it will use notes arraylist*/
    fun restoreNote(position: Int){
        notes.add(trash[position])
        trash.removeAt(position)
        notifyItemRemoved(position)
    }
    //element look in list
    class CustomViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val title_field = view.findViewById(R.id.note_title) as TextView
        val desc_field = view.findViewById(R.id.note_desc) as TextView
        val right_icon = view.findViewById(R.id.swipe_right_icon) as ImageView
        val left_icon = view.findViewById(R.id.swipe_left_icon) as ImageView
        val view_foreground = view.findViewById(R.id.notes_view_foreground) as RelativeLayout

        init {
            //change background view icon
            if (opened_notes){
                updateImages(R.drawable.ic_delete_forever,R.drawable.ic_delete)
                //set ability to click and popup note activity
                view.setOnClickListener {
                    selected_item = adapterPosition
                    view.context.startActivity(Intent(view.context, NoteActivity()::class.java))
                }
            }else {
                updateImages(R.drawable.ic_swipe_restore,R.drawable.ic_delete_forever)
            }
        }
        private fun updateImages(img_left: Int, img_right: Int){
            left_icon.setImageResource(img_left)
            right_icon.setImageResource(img_right)
        }
    }
}