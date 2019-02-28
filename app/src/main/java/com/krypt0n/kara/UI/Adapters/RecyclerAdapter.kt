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
import com.krypt0n.kara.Model.Note
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.*
import java.util.regex.Pattern

class RecyclerAdapter(private val list : ArrayList<Note>) : RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder>() {
    // list item parameters
    override fun onBindViewHolder(holder : CustomViewHolder, position : Int) {
        val title = list[position].title
        var text = list[position].text
        holder.titleField.text = title
        // pattern for cutting description length for RecyclerView item
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
    /**
     * remove note from list on swipe
     * @param position
     */
    fun removeItem(position: Int){
        list.removeAt(position)
        updateFiles()
        notifyItemRemoved(position)
    }

    /**
     * move to trash item on swipe
     * @param position
     */
    fun moveToTrash(position: Int){
        trash.add(notes[position])
        notes.removeAt(position)
        updateFiles()
        notifyItemRemoved(position)
    }

    /**
     * restore note on swipe
     * @param position
     */
    fun restoreNote(position: Int){
        notes.add(trash[position])
        trash.removeAt(position)
        updateFiles()
        notifyItemRemoved(position)
    }
    private fun updateFiles(){
        writeFile("notes",notes)
        writeFile("trash",trash)
    }
    class CustomViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val titleField = view.findViewById(R.id.note_title) as TextView
        val descField = view.findViewById(R.id.note_desc) as TextView
        val viewForeground = view.findViewById(R.id.notes_view_foreground) as RelativeLayout

        init {
            updateImages()
            if (openedNotes){
                // ability to click and start note activity
                view.setOnClickListener {
                    selected_item = adapterPosition
                    view.context.startActivity(Intent(view.context, NoteActivity()::class.java))
                }
            }
        }
        //change background view icons
        private fun updateImages(){
            val rightIcon = if (openedNotes)
                R.drawable.ic_delete
            else
                R.drawable.ic_delete_forever
            val leftIcon = if (openedNotes)
                R.drawable.ic_delete_forever
            else
                R.drawable.ic_restore
            view.findViewById<ImageView>(R.id.swipe_left_icon).setImageResource(leftIcon)
            view.findViewById<ImageView>(R.id.swipe_right_icon).setImageResource(rightIcon)
        }
    }
}