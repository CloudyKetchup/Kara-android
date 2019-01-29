package com.krypt0n.kara.Cloud

import android.support.v7.app.AppCompatActivity
import com.krypt0n.kara.Cloud.Account
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.MongoClient
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class MongoDatabase {
    val ip = "196.168.0.14"
    val port = 27017
    val mongo_client = MongoClient(ip,port)
    val database = mongo_client.getDB("Kara")
    val users = database.getCollection("users")
    var temp_data = users.findOne()
    lateinit var user : BasicDBObject
    lateinit var initial_user : BasicDBObject
    lateinit var acc : BasicDBObject
    lateinit var local_acc : Account
    lateinit var config : JSONObject
    val acc_file = File("/acc_config.json")
    var mongo_connected = false

//    init {
//        if (acc_file.exists())
//            loadConfig()
//        else
//            createConfig()
//    }
    fun signIn(name : String,password: String){
        if (mongo_connected)
            acc = temp_data.get(name) as BasicDBObject
        else
//            acc = config.get(name) as BasicDBObject
        if (acc != null) {
            val acc_name = acc.get("name") as String
            val acc_pass = acc.get("password") as String
            if (acc_pass == password){
                local_acc = Account(acc_name, acc_pass)
//                updateConfig(acc_name,acc_pass)
            }else{
                //wrong password banner
            }
        }
    }
    fun signUp(login : String,password: String){
        if (nameExist(temp_data, login)){
            user = BasicDBObject()
                .append("name",login)
                .append("password",password)
            initial_user = BasicDBObject(login,BasicDBObject()
                .append("name",login)
                .append("password",password))
            if (temp_data == null)
                users.insert(initial_user)
            else{
                temp_data.put(login,user)
                users.update(BasicDBObject(),temp_data)
            }
            temp_data = users.findOne()
            signIn(login,password)
        }
    }
    fun nameExist(db_object : DBObject,field_text : String) : Boolean {
        val user = db_object.get(field_text) as DBObject
        if (user != null)
            return true
        return false
    }
//    fun createConfig() {
//        config = JSONObject()
//        config.put("user",JSONObject())
//        writeConfig(config)
//    }
//    fun loadConfig() {
//        val parser = JSONParser()
//        config = parser.parse(FileReader(acc_file)) as JSONObject
//    }
//    fun writeConfig(config : JSONObject){
//        val fw  = FileWriter(acc_file)
//        fw.write(config.toJSONString())
//        fw.flush()
//    }
//    fun updateConfig(name : String,password : String){
//        val updated_config = config
//        val user = updated_config.get("user") as JSONObject
//        user.put("name",name)
//        user.put("password",password)
//        writeConfig(updated_config)
//    }
}