package com.krypt0n.kara

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.krypt0n.kara.Logic.Data
import com.krypt0n.kara.Logic.Note
import com.krypt0n.kara.UI.ListItem
import com.krypt0n.kara.UI.MyAdapter
import com.krypt0n.kara.UI.NotesView
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recycler_view: RecyclerView
    private var storage_permission : Int = 1
    private lateinit var d : Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var navigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(navListener)

        d = Data()

        loadFile("notes",d.notes)

        recycler_view.adapter = MyAdapter(d.notes_list,this)
//        openFragment(NotesView(R.layout.notes_fragment))
    }

    private var navListener = OnNavigationItemSelectedListener { item ->
        lateinit var selected_fragment : NotesView
        when (item.itemId) {
            R.id.notes -> {
                selected_fragment = NotesView(R.layout.notes_fragment)
                openFragment(selected_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.trash -> {
                selected_fragment = NotesView(R.layout.trash_fragment)
                openFragment(selected_fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun loadFile(file : String, list : ArrayList<Note>){
        val fis = FileInputStream("$filesDir/$file")
        val ois = ObjectInputStream(fis)
        list.equals(ois.read())
        for (i in d.notes){
            var l_item = ListItem(i.title,i.text)
            d.notes_list.add(l_item)
        }
    }
}


