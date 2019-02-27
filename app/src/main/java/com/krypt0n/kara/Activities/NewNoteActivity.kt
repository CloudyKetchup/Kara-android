package com.krypt0n.kara.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.krypt0n.kara.R
import com.krypt0n.kara.Model.Note
import com.krypt0n.kara.Repository.notes
import kotlinx.android.synthetic.main.activity_new_note.*
import kotlinx.android.synthetic.main.note_toolbar.*

class NewNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        note_back_arrow.setOnClickListener {
            finish()
        }
    }
    fun saveNote(v : View){
        if (title.isEmpty())
            title_field.error = "Field cannot be empty"
        else{
            //put note object in list
            notes.add(Note(title_field.text.toString(), text_field.text.toString()))
            //finish this activity
            finish()
        }
    }
    fun exit(){
        finish()
    }
}