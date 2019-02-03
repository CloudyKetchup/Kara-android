package com.krypt0n.kara

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val title_field = findViewById<TextInputEditText>(R.id.note_title_field)
        val text_field = findViewById<TextInputEditText>(R.id.note_text_field)
        val note = notes[selected_item]
        title_field.setText(note.title)
        text_field.setText(note.text)
//        text_field.setText(notes[selected_item].title)
    }
}