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
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.krypt0n.kara.Cloud.Account
import com.krypt0n.kara.Cloud.Account.config
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
import java.io.FileWriter
import java.io.PrintStream
import java.net.InetSocketAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bottom navigation
        bottom_navigation.apply {
            enableShiftingMode(false)
            enableAnimation(false)
            onNavigationItemSelectedListener = navListener
        }
        startupTasks()
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
    //save all changes when app is closed
    override fun onStop() {
        writeFile("$filesDir/notes", notes)
        writeFile("$filesDir/trash", trash)
        if (loggedIn){
            Cloud.apply {
                upload("notes","$filesDir/notes")
                upload("trash","$filesDir/trash")
            }
        }
        super.onStop()
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
            replace(R.id.fragment_container,fragment)
            addToBackStack(null)
            commit()
        }
    }
    private fun createSettingsFile(){
        Thread {
            settings = JsonObject().apply {
                addProperty("cloudSync", false)
                addProperty("lightTheme", false)
            }
            writeSettingsFile()
        }.start()
    }
    //load settings file
    private fun loadSettings(){
        Thread {
            settings = (JsonParser().parse(FileReader("$filesDir/settings.json")) as JsonObject)
                .apply {
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
            name = user.get("name").toString()
            email = user.get("email").toString()
            password = user.get("password").toString()
            loggedIn = true
        }
    }
    //account dialog
    private fun showPopup(){
        Dialog(this).apply {
            setContentView(R.layout.account_popup)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            account_name_field.text = Account.name
            account_email_field.text = Account.email
            close_popup.setOnClickListener {
                dismiss()
            }
            logout_button.setOnClickListener {
                logOut()
                dismiss()
            }
        }.show()
    }
    //startupTasks tasks
    private fun startupTasks(){
        //check device connection to internet for future tasks
        checkInternet()
        //check if server is alive
        if (internetAvailable)
            checkServer()
        filesDirectory = filesDir
        //load notes from file to and place them in ArrayList
        loadFile("$filesDir/notes")
        loadFile("$filesDir/trash")
        //account/settings initialization,also from file if exist
        if (File("$filesDir/acc_config.json").exists())
            loadAccount()
        if (File("$filesDir/settings.json").exists())
            loadSettings()
        else
            createSettingsFile()
    }
    //check device internet connection
    private fun checkInternet(){
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected)
                internetAvailable = true
        }
    }
    //check if server is alive
    private fun checkServer(){
        Thread {
            try{
                Socket().apply {
                    connect(InetSocketAddress(ip, 12000))
                    serverOnline = true
                    close()
                }
            }catch (e : Exception){
                serverOnline = false
            }
        }.start()
    }
    //log out procedure,will delete all data
    private fun logOut(){
        Thread {
            try {
                notes.clear()
                trash.clear()
                //files to be deleted
                val files = arrayOf("notes", "trash", "acc_config.json")
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