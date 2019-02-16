package com.krypt0n.kara.Activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.multidex.MultiDex
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.krypt0n.kara.Cloud.Account
import com.krypt0n.kara.Cloud.Database
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.loadFile
import com.krypt0n.kara.Repository.notes
import com.krypt0n.kara.Repository.trash
import com.krypt0n.kara.Repository.writeFile
import com.krypt0n.kara.UI.Fragments.NotesFragment
import com.krypt0n.kara.UI.Fragments.TrashFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var database : Database
    lateinit var account : Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bottom navigation
        bottom_navigation.apply {
            enableShiftingMode(false)
            onNavigationItemSelectedListener = navListener
        }
//        if (internetAvailable())
//            database = Database(filesDir).apply {
//                mongoConnected = true
//            }
        loadFile("$filesDir","notes")
        loadFile("$filesDir","trash")
//        loadAccount()
        openFragment(NotesFragment())
    }
    //multi dex
    override fun attachBaseContext(base : Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    //go to notes fragment after saved a note,etc...
    override fun onResume() {
        openFragment(NotesFragment())
        OnNavigationItemSelectedListener@
        super.onResume()
    }
    override fun onBackPressed() {
        finish()
    }
    //save all changes when app is closed
    override fun onStop() {
        writeFile("$filesDir/notes", notes)
        writeFile("$filesDir/trash", trash)
        super.onStop()
    }
    private var navListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.notes -> {
                openFragment(NotesFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.trash -> {
                openFragment(TrashFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.create_note -> {
                startActivity(Intent(this, NewNoteActivity()::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.settings -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
//                if (account.name != null)
//                    showPopup()
//                else
                startActivity(Intent(this,LoginActivity()::class.java))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    //open fragment containing note list (notes/trash)
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }
    //use account config from device if exist
    private fun loadAccount() {
        //assign config from database if not null
        val config = database.config
        //and get all data required
        val user = config.get("user") as JSONObject
        val name = user.get("name") as String
        val password = user.get("password") as String
        database.signIn(name, password)
        account = database.localAccount
    }
    fun logOut(v : View){

    }
    private fun showPopup(){

    }
    //check device internet connection
    private fun internetAvailable(): Boolean{
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}