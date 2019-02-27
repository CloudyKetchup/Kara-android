package com.krypt0n.kara.UI.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.openedNotes

class EmptyListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(
            R.layout.empty_list_fragment,
            container,
            false
        ) as View
        view.findViewById<ImageView>(R.id.empty_list_image).setImageResource(
            if (openedNotes)
                R.drawable.ic_empty_box
            else
                R.drawable.ic_empty_paper
        )
        return view
    }
}