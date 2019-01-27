package com.krypt0n.kara.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.FileInputStream

class NotesView() : Fragment() {
    private var layout : Int = 0

    @SuppressLint("ValidFragment")
    constructor(l: Int) : this() {
        layout = l
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout,container,false)
    }
}