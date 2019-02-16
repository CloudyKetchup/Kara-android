package com.krypt0n.kara.Cloud

import com.mongodb.*
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter

// MongoDB database
class Database(files_dir : File) {
    private val ip = "92.181.71.184"
    private val port = 27017
    private lateinit var users : DBCollection     //mongo collection
    private lateinit var temp_data : DBObject     //get collection for future use
    lateinit var acc : BasicDBObject
    lateinit var local_acc : Account
    lateinit var config : JSONObject
    val acc_file = File("$files_dir/acc_config.json")
    var mongo_connected = false

    init {
//        Thread {
            val mongo_client = MongoClient(ip, port)     //client used for connection
            val database = mongo_client.getDB("Kara")   //database
            users = database.getCollection("users")
            temp_data = users.findOne()
            if (acc_file.exists())
                loadConfig()
            else
                createConfig()
//        }.start()
    }
    fun signIn(name : String,password: String){
        acc = if (mongo_connected)
            temp_data.get(name) as BasicDBObject
        else
            config.get(name) as BasicDBObject
        val acc_name = acc.get("name") as String
        val acc_pass = acc.get("password") as String
        if (acc_pass == password){
            local_acc = Account(acc_name, acc_pass)
            updateConfig(acc_name,acc_pass)
        }else{
            //wrong password banner
        }
    }
    fun signUp(login : String,password: String){
        users.insert(
            BasicDBObject(
                login, BasicDBObject()
                    .append("name", login)
                    .append("password", password)
            )
        )
        temp_data = users.findOne()
        signIn(login,password)
    }
    fun nameExist(field_text : String) : Boolean {
        val user = temp_data.get(field_text) as DBObject
        if (user != null)
            return true
        return false
    }
    private fun createConfig() {
        config = JSONObject()
        config.put("user",JSONObject())
        writeConfig(config)
    }
    private fun loadConfig() {
        val fr = FileReader(acc_file)
        config = fr.read() as JSONObject
    }
    private fun writeConfig(config : JSONObject){
        val fw  = FileWriter(acc_file)
        fw.write(config.toString())
        fw.flush()
    }
    private fun updateConfig(name : String, password : String){
        val updated_config = config
        val user = updated_config.get("user") as JSONObject
        user.put("name",name)
        user.put("password",password)
        writeConfig(updated_config)
    }
}