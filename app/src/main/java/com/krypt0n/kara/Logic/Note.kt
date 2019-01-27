package com.krypt0n.kara.Logic

import java.text.SimpleDateFormat
import java.util.*

class Note (title : String,text : String){
    var title= title
    var text = text
    var last_modified = toSimpleString(Date())

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd/MM/yyy").format(this)
    }
    override fun toString() : String {
        return "Title = $title Text = $text Date modified = $last_modified"
    }
}