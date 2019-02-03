package com.krypt0n.kara

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View

class NewNoteActivity : AppCompatActivity() {
    private lateinit var title_field : TextInputEditText
    private lateinit var text_field  : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        title_field = findViewById(R.id.title_field)
        text_field  = findViewById(R.id.text_field)
    }
    fun saveNote(v : View){
        val title = title_field.text.toString().trim()
        val text  = text_field.text.toString().trim()
        if (title.isEmpty())
            title_field.error = "Field cannot be empty"
        else{
            //put note object in list
            notes.add(Note(title,text))
            writeFile("$filesDir")
            //finish this activity
            finish()
        }
    }
}