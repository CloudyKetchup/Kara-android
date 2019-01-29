package com.krypt0n.kara

import java.text.SimpleDateFormat
import java.util.*

class Note1 (var title: String, var text: String){
    var last_modified = this.toSimpleString(Date())

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd/MM/yyy").format(this)
    }
    override fun toString() : String {
        return "Title = $title Text = $text Date modified = $last_modified"
    }
}