package com.krypt0n.kara.UI

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krypt0n.kara.Note
import com.krypt0n.kara.R

class TrashFragment : Fragment() {
    var trash_list = ArrayList<Note>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.trash_fragment, container, false)
    }
}