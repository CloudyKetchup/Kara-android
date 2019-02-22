package com.krypt0n.kara.Cloud

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.krypt0n.kara.Repository.filesDirectory
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object Account {
    lateinit var email : String
    lateinit var name : String
    lateinit var password : String
    lateinit var config : JsonObject
    private val accountFile = File("$filesDirectory/acc_config.json")

    fun loadConfig(){
        config = JsonParser().parse(FileReader(accountFile)) as JsonObject
    }
    fun createConfig() {
        //create blank json with only user object
        config = JsonObject().apply {
            add("user",JsonObject())
        }
        updateConfig(name, password)
        writeConfig(config)
    }
    private fun writeConfig(config: JsonObject) {
        FileWriter(accountFile).apply {
            write(config.toString())
            flush()
        }
    }
    private fun updateConfig(name: String, password: String) {
        (config.get("user") as JsonObject).apply {
            addProperty("name", name)
            addProperty("email", email)
            addProperty("password", password)
        }
        writeConfig(config)
    }
}