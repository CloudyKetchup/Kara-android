package com.krypt0n.kara

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity

class NoteActivity : AppCompatActivity() {
    private lateinit var note : Note
    private lateinit var title_field : TextInputEditText
    private lateinit var text_field  : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        note = notes[selected_item]
        title_field = findViewById(R.id.note_title_field)
        text_field  = findViewById(R.id.note_text_field)
        title_field.setText(note.title)
        text_field.setText(note.text)
    }
    override fun onBackPressed() {
        updateNotes()
        finish()
    }
    override fun onStop() {
        updateNotes()
        super.onStop()
    }
    private fun updateNotes(){
        note.title = title_field.text.toString().trim()
        note.text = text_field.text.toString().trim()
        writeFile("$filesDir/notes",notes)
    }
}