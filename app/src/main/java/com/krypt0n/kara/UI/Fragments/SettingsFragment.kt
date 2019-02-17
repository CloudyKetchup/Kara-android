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

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val settingsFile = "${view?.context?.filesDir}/settings.json"
        //set settings switch for synchronization depending on settings file if that exist
        if (File(settingsFile).exists() || JSONObject(settingsFile).get("cloudSync") as Boolean)
            sync_switch.isChecked
        //enable or disable data sync
        cloudSync = sync_switch.isChecked
        return inflater.inflate(
            R.layout.settings_fragment,
            container,
            false
        ) as View
    }
}