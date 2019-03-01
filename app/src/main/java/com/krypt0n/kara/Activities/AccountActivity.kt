package com.krypt0n.kara.Activities

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.krypt0n.kara.Cloud.Account
import com.krypt0n.kara.Cloud.Cloud
import com.krypt0n.kara.Remote.RetrofitClient
import com.krypt0n.kara.Remote.ServletService
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.loadFile
import com.krypt0n.kara.Repository.loggedIn
import com.krypt0n.kara.Repository.serverOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_layout.*
import kotlinx.android.synthetic.main.registration_layout.*

class AccountActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var serverService : ServletService
    private var loginLayoutOpened = false   // will be used for changing login/registration view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        loginLayoutOpened = true

        //init ServletService
        val retrofitClient = RetrofitClient.getInstance()
        serverService = retrofitClient.create(ServletService::class.java)
    }
    //back key adaptation for login layout and registration layout
    override fun onBackPressed() {
        if (loginLayoutOpened)
            finish()
        else {
            setContentView(R.layout.login_layout)
            loginLayoutOpened = true
        }
    }
    //stop all related to this activity threads on exit
    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
    //login action
    fun loginUser(v: View) {
        //login fields
        val email = email_field.text.toString()
        val password = password_field.text.toString()
        //check if any field is empty
        when {
            email.isEmpty() -> email_field.error = "Email cannot be empty"
            password.isEmpty() -> password_field.error = "Password cannot be empty"
            else -> {
                if (serverOnline){
                    loginProcedure(email,password)
                }else
                    toastMessage(login_activity_layout,"Server offline,try again later")
            }
        }
    }

    /**
     * login logic
     * @param email
     * @param password
     */
    private fun loginProcedure(email: String, password: String) {
        compositeDisposable.add(serverService.loginUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                //check if password is correct or email is registered
                when (response) {
                    "Wrong password" -> password_field.error = "Wrong Password"
                    "Email not exist" -> password_field.error = "Account not exist"
                    else -> {
                        Account.apply {
                            name = response.toString().trim()
                            this.email = email.trim()
                            this.password = password.trim()
                            createConfig()
                        }
                        Cloud.apply {
                            Thread {
                                syncFile("notes", "$filesDir/notes")
                                loadFile("notes")
                            }.start()
                            Thread {
                                syncFile("trash", "$filesDir/trash")
                                loadFile("trash")
                            }.start()
                        }
                        loggedIn = true
                        finish()
                    }
                }
            })
    }
    fun registerUser(v: View) {
        //text from fields
        val email = registration_email_field.text.toString()
        val name = registration_name_field.text.toString()
        val password = registration_password_field.text.toString()
        //check if any field is empty
        when {
            email.isEmpty() -> registration_email_field.error = "Email cannot be empty"
            name.isEmpty() -> registration_name_field.error = "Name cannot be empty"
            password.isEmpty() -> registration_password_field.error = "Password cannot be empty"
            else -> {
//                if (serverOnline)
                    registerProcedure(name,email,password)
//                else
//                    toastMessage(registration_layout,"Server offline,try again later")
            }
        }
    }

    /**
     * registration logic
     * @param name
     * @param email
     * @param password
     */
    private fun registerProcedure(name : String,email : String,password: String){
        //will register account
        compositeDisposable.add(serverService.registerUser(email,name,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                if (response == "Account already exist")
                    registration_email_field.error = response
                else if (response == "Registration success"){
                    //login to new account
                    loginProcedure(email,password)
                }
            })
    }
    //create account dialog
    fun registrationView(v : View){
        setContentView(R.layout.registration_layout)
        loginLayoutOpened = false
    }
    private fun toastMessage(layout: ConstraintLayout, message: String){
        Toast.makeText(layout.context,message,Toast.LENGTH_SHORT).show()
    }
}