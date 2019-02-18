package com.krypt0n.kara.Cloud

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.krypt0n.kara.Repository.mongoConnected
import com.mongodb.*
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_activity.*
import org.bson.Document
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.PrintStream

// MongoDB database
class Database(val activity: AppCompatActivity) {
    lateinit var client : MongoClient   //client for connecting to Database
    private lateinit var users : DBCollection     //collection of users
    lateinit var tempData : DBObject     //get collection for future use
    lateinit var acc : BasicDBObject
    lateinit var localAccount : Account
    lateinit var config : JSONObject
    val accFile = File("${activity.filesDir}/acc_config.json")

    init {
//        Thread {
            if (accFile.exists())
                config = JSONObject("${activity.filesDir}/acc_config.json")
            else
                createConfig()
            try {
                client = MongoClient("192.168.0.14",27017)
                mongoConnected = true
            }catch (e : Exception){
                val f = File("${activity.filesDir}/log.txt")
                val p = PrintStream(f)
                e.printStackTrace(p)
            }
            if (mongoConnected) {
                Snackbar.make(activity.root_layout,"Mongo",Snackbar.LENGTH_LONG).show()
                val database = client.getDatabase("Kara")   //database
                users = database.getCollection("users") as DBCollection
                tempData = users.findOne()!!
            }
//        }.start()
    }
    fun signIn(name : String,password: String){
        acc = if (mongoConnected)
            tempData.get(name) as BasicDBObject
        else
            config.get(name) as BasicDBObject
        if (acc != null) {
            val accName = acc.get("name") as String
            val accPassword = acc.get("password") as String
            if (accPassword == password) {
                localAccount = Account(accName, accPassword)
                updateConfig(accName, accPassword)
                activity.finish()
                Snackbar.make(activity.root_layout, "Welcome back ;)", Snackbar.LENGTH_LONG).show()
            } else {
                activity.password_field.error = "Wrong password"
            }
        }else
            activity.login_field.error = "This account does not exist"
    }
    fun signUp(login : String,password: String){
        users.insert(
            BasicDBObject(
                login, BasicDBObject()
                    .append("name", login)
                    .append("password", password)
            )
        )
        tempData = users.findOne()!!
        signIn(login,password)
    }
    fun nameExist(field_text : String) : Boolean {
        if (tempData.get(field_text) != null)
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