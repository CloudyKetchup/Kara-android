package com.krypt0n.kara.UI.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
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
        val themeSwitch = view.findViewById<Switch>(R.id.theme_switch)
        when {
            cloudSync  -> syncSwitch.isChecked = true
            lightTheme -> themeSwitch.isChecked = true
        }
        //enable/disable cloud synchronization and lightTheme with SwitchToggle in settings
        syncSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                cloudSync = true
                updateSettingsFile()
            }else{
                cloudSync = false
                updateSettingsFile()
            }
        }
        themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                lightTheme = true
                updateSettingsFile()
            }else{
                lightTheme = false
                updateSettingsFile()
            }
        }
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