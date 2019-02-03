package com.krypt0n.kara

import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.PrintStream

var notes = ArrayList<Note>()
var trash = ArrayList<Note>()
var selected_item : Int = 0

//write file to local storage
fun writeFile(directory : String){
    Thread {
        try {
            val oos = ObjectOutputStream(FileOutputStream("$directory/notes"))
            oos.writeObject(notes)
        } catch (e: Exception) {
            //save log file with exception
            val f = File("$directory/log.txt")
            val p = PrintStream(f)
            e.printStackTrace(p)
        }
    }.start()
}