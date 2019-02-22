package com.krypt0n.kara.UI.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.openedNotes

class EmptyListFragment : Fragment() {
    init {
        openedNotes = true
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(
            R.layout.empty_list_fragment,
            container,
            false
        ) as View
        return v
    }
}