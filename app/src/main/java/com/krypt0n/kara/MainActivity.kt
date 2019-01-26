package com.krypt0n.kara

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var drawer  : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer = findViewById(R.id.drawer_layout)
        var nav_view : NavigationView = findViewById(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)
        var toolbar : Toolbar = findViewById(R.id.toolbar)
        var toggle = ActionBarDrawerToggle(
            this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        setSupportActionBar(toolbar)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,NotesFragment()).commit()
            nav_view.setCheckedItem(R.id.notes_layout)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.notes -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,NotesFragment()).commit()
            }
            R.id.trash -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,TrashFragment()).commit()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START))
            drawer!!.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
}