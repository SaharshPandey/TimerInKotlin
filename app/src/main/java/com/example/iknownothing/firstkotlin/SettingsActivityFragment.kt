package com.example.iknownothing.firstkotlin

import android.app.Fragment
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsActivityFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}