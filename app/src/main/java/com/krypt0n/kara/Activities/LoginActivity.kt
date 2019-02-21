package com.krypt0n.kara.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.krypt0n.kara.Network.RetrofitClient
import com.krypt0n.kara.Network.ServerService
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.registration_layout.*

class LoginActivity : AppCompatActivity(){
    private val compositeDisposable = CompositeDisposable()
    private lateinit var serverService : ServerService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        //init ServerService
        val retrofitClient = RetrofitClient.getInstance()
        serverService = retrofitClient.create(ServerService::class.java)
    }
    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
    fun signUpView(v : View){
        val view = LayoutInflater.from(login_activity_layout.context)
            .inflate(R.layout.registration_layout,null)
    }
    fun loginUser(v : View){
        val email = findViewById<TextInputEditText>(R.id.email_field).text.toString()
        val password = findViewById<TextInputEditText>(R.id.password_field).text.toString()
        when {
            email.isEmpty() -> email_field.error = "Email cannot be empty"
            password.isEmpty() -> password_field.error = "Password cannot be empty"
            else -> {
                if (serverOnline){
                    compositeDisposable.add(serverService.loginUser(email,password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { response ->
                            Toast.makeText(login_activity_layout.context, response, Toast.LENGTH_SHORT).show()
                        })
                }else
                    Toast.makeText(login_activity_layout.context,"You are offline", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun registerUser(v : View){
        val email = registration_email_field.text.toString()
        val name  = registration_name_field.text.toString()
        val password = registration_password_field.text.toString()
        when {
            email.isEmpty() -> registration_email_field.error = "Email cannot be empty"
            name.isEmpty() -> registration_name_field.error = "Name cannot be empty"
            password.isEmpty() -> registration_password_field.error = "Password cannot be empty"
            else -> {
                compositeDisposable.add(serverService.registerUser(email,name,password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { response ->
                        Toast.makeText(login_activity_layout.context,response,Toast.LENGTH_SHORT).show()
                    })
            }
        }
    }
}