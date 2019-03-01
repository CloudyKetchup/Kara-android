package com.krypt0n.kara.UI.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import com.krypt0n.kara.R
import com.krypt0n.kara.Repository.*

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(
            R.layout.settings_fragment,
            container,
            false
        ) as View
        val syncSwitch = view.findViewById<Switch>(R.id.sync_switch)
        if (cloudSync)
            syncSwitch.isChecked = true
        //enable/disable cloud synchronization with SwitchToggle in settings
        if (loggedIn) {
            syncSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    cloudSync = true
                    updateSettingsFile()
                } else {
                    cloudSync = false
                    updateSettingsFile()
                }
            }
        }else
            Toast.makeText(this.context,"Log in first",Toast.LENGTH_SHORT).show()
        return view
    }
    private fun updateSettingsFile(){
        settings.apply {
            addProperty("cloudSync", cloudSync)
            addProperty("lightTheme", lightTheme)
        }
        writeSettingsFile()
    }
}