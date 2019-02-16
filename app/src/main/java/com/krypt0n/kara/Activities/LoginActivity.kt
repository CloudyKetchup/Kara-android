package com.krypt0n.kara.Activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.krypt0n.kara.Cloud.Database
import com.krypt0n.kara.R

class LoginActivity : AppCompatActivity() {
    lateinit var login : String
    lateinit var password : String
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.login_activity)
        login = findViewById<TextInputEditText>(R.id.login_field).text.toString().trim()
        password = findViewById<TextInputEditText>(R.id.password_field).text.toString().trim()
    }
    //function calling sign in from database
    fun signIn(v : View){
        Database(filesDir).apply {
            when {
                login.isEmpty() -> findViewById<TextInputEditText>(R.id.login_field).error = "Field cannot be empty"
                nameExist(login) -> signIn(login,password)
                else -> findViewById<TextInputEditText>(R.id.login_field).error = "Account does not exist"
            }
        }
    }
    //function calling sign up from database
    fun SignUp(v : View){
        Database(filesDir).apply {
            if (nameExist(login))
                findViewById<TextInputEditText>(R.id.login_field).error = "This account already exist"
            else
                signUp(login,password)
        }
    }
}