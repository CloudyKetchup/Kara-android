package com.krypt0n.kara.Activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.design.widget.Snackbar
import android.support.multidex.MultiDex
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.krypt0n.kara.Cloud.Account
import com.krypt0n.kara.Cloud.Cloud
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.*
import com.krypt0n.kara.UI.Fragments.EmptyListFragment
import com.krypt0n.kara.UI.Fragments.NotesFragment
import com.krypt0n.kara.UI.Fragments.SettingsFragment
import com.krypt0n.kara.UI.Fragments.TrashFragment
import kotlinx.android.synthetic.main.account_popup.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.PrintStream
import java.net.InetAddress

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        startupTasks()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bottom navigation
        bottom_navigation.apply {
            enableShiftingMode(false)
            enableAnimation(false)
            onNavigationItemSelectedListener = navListener
        }
         //will open fragment depending on notes list size
        if (notes.isEmpty()) {
            openedNotes = true
            //fragment with empty box if notes are empty
            openFragment(EmptyListFragment())
        }else
            //fragment with notes list if have at least on note
            openFragment(NotesFragment())
    }
    //multi dex
    override fun attachBaseContext(base : Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    //go to notes fragment after saved a note,etc...
    override fun onResume() {
        if (notes.isEmpty())
            openFragment(EmptyListFragment())
        else
            openFragment(NotesFragment())
        super.onResume()
    }
    override fun onBackPressed() {
        finish()
    }
    private var navListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.notes -> {
                if (notes.isEmpty()) {
                    openedNotes = true
                    openFragment(EmptyListFragment())
                }else
                    openFragment(NotesFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.trash -> {
                if (trash.isEmpty()) {
                    openedNotes = false
                    openFragment(EmptyListFragment())
                }else
                    openFragment(TrashFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.create_note -> {
                startActivity(Intent(this, NewNoteActivity()::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.settings -> {
                openFragment(SettingsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                if (File("$filesDir/acc_config.json").exists())
                    showPopup()
                else
                    startActivity(Intent(this,AccountActivity()::class.java))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    /**
     * Open fragment containing note list (notes/trash)
     * @param fragment
     */
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            //replace current fragment with chosen one
            replace(R.id.fragment_container,fragment)
            addToBackStack(null)
            commit()
        }
    }
    private fun createSettingsFile(){
        settings = JsonObject().apply {
            addProperty("cloudSync", false)
            addProperty("lightTheme", false)
        }
        writeSettingsFile()
    }
    //load settings file
    private fun loadSettings(){
        Thread {
            //initialize settings JsonObject and parse it from file
            settings = (JsonParser().parse(FileReader("$filesDir/settings.json")) as JsonObject)
                .apply {
                    //get properties
                    cloudSync = get("cloudSync").asBoolean
                    lightTheme = get("lightTheme").asBoolean
                }
        }.start()
    }
    //load account data from file acc_config.json
    private fun loadAccount() {
        Account.apply {
            loadConfig()
            //get all data required
            val user = config.get("user") as JsonObject
            name = user.get("name").toString().trim('"')
            email = user.get("email").toString().trim('"')
            password = user.get("password").toString().trim('"')
            loggedIn = true
        }
    }
    //account dialog
    private fun showPopup(){
        Dialog(this).apply {
            //set view
            setContentView(R.layout.account_popup)
            //semitransparent background
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //setup fields text
            account_name_field.text = Account.name
            account_email_field.text = Account.email
            //close popup when click X button
            close_popup.setOnClickListener {
                dismiss()
            }
            //logout procedure on click
            logout_button.setOnClickListener {
                logOut()
                dismiss()
            }
        }.show()
    }
    //startupTasks tasks
    private fun startupTasks(){
        //check if server is alive
        checkServer()
        filesDirectory = filesDir
        //load notes from file to and place them in ArrayList
        loadFile("notes")
        loadFile("trash")
        //account/settings initialization,also from file if exist
        if (File("$filesDir/acc_config.json").exists())
            loadAccount()
        if (File("$filesDir/settings.json").exists())
            loadSettings()
        else
            createSettingsFile()
    }
    //check device internet connection
    private fun checkInternet() : Boolean {
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                internetAvailable = true
                true
            }else
                false
        }
    }
    //check if server is alive
    private fun checkServer(){
        Thread {
            if (checkInternet()) {
                try {
                    //check if server is reachable
                    if (InetAddress.getByName(ip).isReachable(300)) {
                        serverOnline = true
//                        runOnUiThread {
//                            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
//                        }
                    }
                } catch (e: IOException) {
                    serverOnline = false
//                    runOnUiThread {
//                        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
//                    }
                    e.printStackTrace()
                }
            }
        }.start()
    }
    //download data from cloud when click on sync button
    fun sync(v : View) {
        if (loggedIn || cloudSync) {
            Cloud.apply {
                //separate threads because we can have only one FTP connection
                Thread {
                    syncFile("notes", "$filesDir/notes")
                    loadFile("notes")
                }.start()
                Thread {
                    syncFile("trash", "$filesDir/trash")
                    loadFile("trash")
                    openFragment(NotesFragment())
                }.start()
            }
        }else {
            val message = "Log in first and enable cloudSync in settings"
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
    }
    //log out procedure,will delete all data
    private fun logOut(){
        Thread {
            try {
                notes.clear()
                trash.clear()
                //files to be deleted
                val files = arrayOf("notes", "trash", "acc_config.json","settings.json")
                for (i in files.indices) {
                    File("$filesDir/${files[i]}").delete()
                }
            } catch (e: Exception) {
                val f = File("$filesDir/log.txt")
                val p = PrintStream(f)
                e.printStackTrace(p)
            } finally {
                openFragment(NotesFragment())
                Snackbar.make(root_layout, "Good Bye ;)", Snackbar.LENGTH_LONG).show()
            }
        }.start()
    }
}