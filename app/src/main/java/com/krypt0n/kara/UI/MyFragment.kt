package com.krypt0n.kara

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MyFragment() : Fragment() {
    private var layout : Int = 0

    @SuppressLint("ValidFragment")
    constructor(l : Int) : this() {
        layout = l
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout,container,false)
    }
}