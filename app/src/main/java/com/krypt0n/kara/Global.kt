package com.krypt0n.kara

import java.io.*

var notes = ArrayList<Note>()
var trash = ArrayList<Note>()
var selected_item : Int = 0

//write file to local storage
fun writeFile(directory : String,list : ArrayList<Note>){
    Thread {
        try {
            val oos = ObjectOutputStream(FileOutputStream("$directory"))
            oos.writeObject(list)
        } catch (e: Exception) {
            //save log file with exception
            val f = File("$directory/log.txt")
            val p = PrintStream(f)
            e.printStackTrace(p)
        }
    }.start()
}