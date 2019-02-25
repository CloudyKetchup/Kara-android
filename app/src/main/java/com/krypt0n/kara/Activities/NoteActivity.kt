package com.krypt0n.kara.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krypt0n.kara.R
import com.krypt0n.kara.Model.Note
import com.krypt0n.kara.Repository.notes
import com.krypt0n.kara.Repository.selected_item
import com.krypt0n.kara.Repository.writeFile
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    private lateinit var note : Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        note = notes[selected_item]
        note_title_field.setText(note.title)
        note_text_field.setText(note.text)
    }
    override fun onBackPressed() {
        updateNotes()
        finish()
    }
    override fun onStop() {
        updateNotes()
        super.onStop()
    }
    private fun updateNotes() {
        note.title = note_title_field.text.toString().trim()
        note.text = note_text_field.text.toString().trim()
        writeFile("$filesDir/notes", notes)
    }
}