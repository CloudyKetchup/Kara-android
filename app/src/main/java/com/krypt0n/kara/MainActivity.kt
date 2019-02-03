package com.krypt0n.kara

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.multidex.MultiDex
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.krypt0n.kara.Cloud.Account
import com.krypt0n.kara.Cloud.MongoDatabase
import com.krypt0n.kara.UI.Fragments.NotesFragment
import com.krypt0n.kara.UI.Fragments.TrashFragment
import org.json.JSONObject
import java.io.*

class MainActivity : AppCompatActivity() {
    lateinit var database : MongoDatabase
    lateinit var account : Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bottom navigation
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //bottom navigation action listener
        navigationView.setOnNavigationItemSelectedListener(navListener)

//        if (internetAvailable())
//            database = MongoDatabase().apply {
//                mongo_connected = true
//            }
//        loadAccount()
        loadFile("$filesDir","notes")
        loadFile("$filesDir","trash")
    }
    //multi dex
    override fun attachBaseContext(base : Context){
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onResume() {
        openFragment(NotesFragment())
        super.onResume()
    }
    override fun onBackPressed() {
        finish()
    }
    private var navListener = OnNavigationItemSelectedListener { item ->
        lateinit var selected_fragment : Fragment
        when (item.itemId) {
            R.id.notes -> {
                selected_fragment = NotesFragment()
                openFragment(selected_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.trash -> {
                selected_fragment = TrashFragment(trash)
                openFragment(selected_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.create_note -> {
                startActivity(Intent(this, NewNoteActivity()::class.java))
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
    //load notes/trash from file on local storage
    private fun loadFile(directory: String,file: String) {
        Thread {
            val f = File("$directory/$file")
            if(f.exists()) {
                try {
                    val temp = ObjectInputStream(FileInputStream(f)).readObject() as ArrayList<Note>
                    if (file.contains("notes")) {
                        notes = temp
                    } else
                        trash = temp
                    openFragment(NotesFragment())
                } catch (e: Exception) {
                    val f = File("$directory/log.txt")
                    val p = PrintStream(f)
                    e.printStackTrace(p)
                }
            }
        }.start()
    }
    private fun loadAccount() {
        val config = database.config
        val user = config.get("user") as JSONObject
        val name = user.get("name") as String
        val password = user.get("password") as String
        database.signIn(name, password)
        account = Account(name, password)
    }
    //check device internet connection
    private fun internetAvailable(): Boolean{
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
class Note (var title: String, var text: String) : Serializable