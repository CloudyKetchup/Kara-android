package com.krypt0n.kara.Activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.krypt0n.kara.Cloud.Database
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.serverOnline
import kotlinx.android.synthetic.main.login_activity.*
import java.net.InetAddress

class LoginActivity : AppCompatActivity() {
    lateinit var login : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        login = findViewById<TextInputEditText>(R.id.login_field).text.toString().trim()
        password = findViewById<TextInputEditText>(R.id.password_field).text.toString().trim()
    }
    //function calling sign in from database
    fun signIn(v : View){
        if (internetAvailable() || serverOnline) {
            Database(filesDir,this).apply {
                when {
                    login.isEmpty() -> login_field.error = "Field cannot be empty"
                    nameExist(login) -> {
                        signIn(login, password)
                        finish()
                    }
                    else -> login_field.error = "Account does not exist"
                }
            }
        }else {
            Snackbar.make(login_activity_layout,"Hmm,something went wrong",Snackbar.LENGTH_LONG).show()
        }
    }
    //function calling sign up from database
    fun signUp(v : View){
        if (internetAvailable() || serverOnline) {
            Database(filesDir,this).apply {
                if (nameExist(login))
                    login_field.error = "This account already exist"
                else {
                    signUp(login, password)
                    finish()
                }
            }
        }
    }
    //check device internet connection
    fun internetAvailable(): Boolean{
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}