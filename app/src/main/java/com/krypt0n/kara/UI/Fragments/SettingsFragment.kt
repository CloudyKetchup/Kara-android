package com.krypt0n.kara.UI.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.cloudSync
import kotlinx.android.synthetic.main.settings_fragment.*
import org.json.JSONObject
import java.io.File
import java.io.PrintStream

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(
            R.layout.settings_fragment,
            container,
            false
        ) as View
        val settingsFile = "${v.context.filesDir}/settings.json"
        //set settings switch for synchronization depending on settings file if that exist
        if (File(settingsFile).exists())
            if (JSONObject(settingsFile).get("cloudSync") as Boolean)
                sync_switch.isChecked
        //enable or disable data sync
        try {
            cloudSync = sync_switch.isChecked
        }catch (e : Exception){
            //save log file with exception
            val f = File("${v.context.filesDir}/log.txt")
            val p = PrintStream(f)
            e.printStackTrace(p)
        }
        return v
    }
}