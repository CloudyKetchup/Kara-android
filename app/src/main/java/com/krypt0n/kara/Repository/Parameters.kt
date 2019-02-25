package com.krypt0n.kara.Repository

import com.krypt0n.kara.Model.Note
import java.io.*

//parameters
var filesDirectory = ""
var selected_item = 0
var openedNotes = false
var serverOnline = false                  //Server Alive
var internetAvailable = false             //Device internet connection
var ftpConnected = false
var logedIn = false
var cloudSync = false                     //Cloud Backup on/off
const val ip = "192.168.1.135"             //Server ip
const val ftpPort = 2221                  //FTP connection port
const val databaseServicePort = 3000      //Server service port

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
//load file from local storage
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
