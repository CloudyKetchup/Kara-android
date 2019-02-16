package com.krypt0n.kara.Activities

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.Note
import com.krypt0n.kara.Repository.notes

class NewNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
    }
    fun saveNote(v : View){
        val title = findViewById<TextInputEditText>(R.id.title_field).text.toString().trim()
        val text  = findViewById<TextInputEditText>(R.id.text_field).text.toString().trim()
        if (title.isEmpty())
            findViewById<TextInputEditText>(R.id.title_field).error = "Field cannot be empty"
        else{
            //put note object in list
            notes.add(Note(title, text))
            //finish this activity
            OnNavigationItemSelectedListener@
            finish()
        }
    }
}