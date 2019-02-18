package com.krypt0n.kara.Activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.krypt0n.kara.Cloud.Database
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.mongoConnected
import com.krypt0n.kara.Repository.serverOnline
import kotlinx.android.synthetic.main.login_activity.*
import java.io.File
import java.io.PrintStream


class LoginActivity : AppCompatActivity() {
    lateinit var login : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
//        login = findViewById<TextInputEditText>(R.id.login_field).text.toString().trim()
        password = findViewById<TextInputEditText>(R.id.password_field).text.toString().trim()
    }
    //function calling sign in from database
    fun signIn(v : View){
        if (serverOnline || mongoConnected) {
            try {
                val database = Database(this)
                when {
                    login_field.text.isNullOrEmpty() -> errorMessage("Field cannot be empty")
                    database.nameExist(login_field.text.toString().trim()) -> database.signIn(login_field.text.toString().trim(), password)
                    else -> errorMessage("Account does not exist")
                }
            } catch (e: Exception) {
                val f = File("$filesDir/log.txt")
                val p = PrintStream(f)
                e.printStackTrace(p)
                Snackbar.make(login_activity_layout, "Hmm,something went wrong", Snackbar.LENGTH_LONG).show()
            }
        } else {
            Snackbar.make(login_activity_layout, "Hmm,something went wrong", Snackbar.LENGTH_LONG).show()
        }
    }
    private fun errorMessage(message : String){
        runOnUiThread {
            login_field.error = message
        }
    }
    //function calling sign up from database
    fun signUp(v : View){
        if (serverOnline || mongoConnected) {
            Database(this).apply {
                if (nameExist(login))
                    login_field.error = "This account already exist"
                else {
                    signUp(login, password)
                    finish()
                }
            }
        }
    }
}