package com.krypt0n.kara

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.krypt0n.kara.Cloud.Account
import com.krypt0n.kara.Cloud.MongoDatabase
import com.krypt0n.kara.UI.NotesFragment
import com.krypt0n.kara.UI.TrashFragment
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var database : MongoDatabase
    private lateinit var account : Account

    var notes = ArrayList<Note>()
    var trash = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navigationView.setOnNavigationItemSelectedListener(navListener)


//        if (internetAvailable(this)){
//            database = MongoDatabase().apply {
//                mongo_connected = true
//            }
//        }
//        loadAccount()
        loadFile("notes")
        openFragment(NotesFragment(notes))
    }

    private var navListener = OnNavigationItemSelectedListener { item ->
        lateinit var selected_fragment : Fragment
        when (item.itemId) {
            R.id.notes -> {
                selected_fragment = NotesFragment(notes)
                openFragment(selected_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.trash -> {
                selected_fragment = TrashFragment()
                openFragment(selected_fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }
    private fun loadFile(file : String){
        val f = File("$filesDir/$file")
        if (f.exists()) {
            val fis = FileInputStream(f)
            val ois = ObjectInputStream(fis)
            ois.use {
                notes = ois.readObject() as ArrayList<Note>
            }
        }
    }
//    private fun loadAccount(){
//        val config = database.config
//        val user = config.get("user") as JSONObject
//        val name = user.get("name") as String
//        val password = user.get("password") as String
//        database.signIn(name,password)
//        account = Account(name, password)
//    }
    fun internetAvailable(activity: AppCompatActivity): Boolean{
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}


