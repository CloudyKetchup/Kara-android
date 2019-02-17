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
    private var users : DBCollection     //mongo collection
    private var tempData : DBObject     //get collection for future use
    lateinit var acc : BasicDBObject
    lateinit var localAccount : Account
    lateinit var config : JSONObject
    val accFile = File("$files_dir/acc_config.json")
    var mongoConnected = false

    init {
//        Thread {
            val mongoClient = MongoClient(ip, port)     //client used for connection
            val database = mongoClient.getDB("Kara")   //database
            users = database.getCollection("users")
            tempData = users.findOne()
            if (accFile.exists())
                config = JSONObject("$files_dir/acc_config.json")
            else
                createConfig()
//        }.start()
    }
    fun signIn(name : String,password: String){
        acc = if (mongoConnected)
            tempData.get(name) as BasicDBObject
        else
            config.get(name) as BasicDBObject
        val accName = acc.get("name") as String
        val accPassword = acc.get("password") as String
        if (accPassword == password){
            localAccount = Account(accName, accPassword)
            updateConfig(accName,accPassword)
        }else{
//            AppCompatActivity().password_field.error = "Wrong password"
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
        tempData = users.findOne()
        signIn(login,password)
    }
    fun nameExist(field_text : String) : Boolean {
        val user = tempData.get(field_text) as DBObject
        if (user != null)
            return true
        return false
    }
    private fun createConfig() {
        config = JSONObject()
        config.put("user",JSONObject())
        writeConfig(config)
    }
    private fun writeConfig(config : JSONObject){
        val fw  = FileWriter(accFile)
        fw.write(config.toString())
        fw.flush()
    }
    private fun updateConfig(name : String, password : String){
        val updatedConfig = config
        val user = updatedConfig.get("user") as JSONObject
        user.put("name",name)
        user.put("password",password)
        writeConfig(updatedConfig)
    }
}