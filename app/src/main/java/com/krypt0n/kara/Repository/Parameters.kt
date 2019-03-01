package com.krypt0n.kara.Repository

import com.google.gson.JsonObject
import com.krypt0n.kara.Cloud.Cloud
import com.krypt0n.kara.Model.Note
import java.io.*

//app parameters,by default are off
lateinit var filesDirectory : File
lateinit var settings : JsonObject
var selected_item = 0
var openedNotes  = false
var serverOnline = false
var internetAvailable = false
var ftpConnected = false
var loggedIn   = false
var cloudSync  = false                     //Cloud Backup on/off
var lightTheme = false
const val ip = "192.168.0.14"            //Server ip

/**
 * save notes file to local storage
 * @param file
 * @param list
 */
fun writeFile(file : String, list : ArrayList<Note>){
    Thread {
        try {
            val oos = ObjectOutputStream(FileOutputStream("$filesDirectory/$file"))
            oos.writeObject(list)
        } catch (e: Exception) {
            //save log file with exception
            val f = File("$filesDirectory/log.txt")
            val p = PrintStream(f)
            e.printStackTrace(p)
        }
    }.start()
    if (cloudSync && internetAvailable)
        Thread{
            Cloud.uploadFile(file,"$filesDirectory/$file")
        }.start()
}
/**
 * load file from local storage
 * @param file
 */
fun loadFile(file: String) {
    Thread {
        val f = File("$filesDirectory/$file")
        if (f.exists()) {
            try {
                val temp = ObjectInputStream(FileInputStream(f)).readObject() as ArrayList<Note>
                if (file.contains("notes")) {
                    notes = temp
                }else
                    trash = temp
            } catch (e: Exception) {
                val f = File("$filesDirectory/log.txt")
                val p = PrintStream(f)
                e.printStackTrace(p)
            }
        }
    }.start()
}
fun writeSettingsFile(){
    Thread {
        try {
            FileWriter("$filesDirectory/settings.json").apply {
                write(settings.toString())
                flush()
            }
        }catch (e : Exception){
            val f = File("$filesDirectory/log.txt")
            val p = PrintStream(f)
            e.printStackTrace(p)
        }
    }.start()
}