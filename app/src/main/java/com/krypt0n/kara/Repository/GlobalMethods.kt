package com.krypt0n.kara.Repository

import java.io.*

var selected_item = 0
var openedNotes = false
var serverOnline = false
var internetAvailable = false
var cloudSync = false
//write file to local storage
fun writeFile(file : String, list : ArrayList<Note>){
    Thread {
        try {
            val oos = ObjectOutputStream(FileOutputStream(file))
            oos.writeObject(list)
        } catch (e: Exception) {
            //save log file with exception
            val f = File("$file/log.txt")
            val p = PrintStream(f)
            e.printStackTrace(p)
        }
    }.start()
}
//load notes/trash from file on local storage
fun loadFile(directory: String,file: String) {
    Thread {
        val f = File("$directory/$file")
        if(f.exists()) {
            try {
                val temp = ObjectInputStream(FileInputStream(f)).readObject() as ArrayList<Note>
                if (file.contains("notes")) {
                    notes = temp
                } else
                    trash = temp
            } catch (e: Exception) {
                val f = File("$directory/log.txt")
                val p = PrintStream(f)
                e.printStackTrace(p)
            }
        }
    }.start()
}
