package com.example.mynote

import android.content.Context
import android.preference.PreferenceManager


object Utils {

        fun writeStringToPreferences(key: String, value: String, activity: Context?) {
            if (activity == null) {
                return
            }
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedPrefEditor = sharedPreferences.edit()
            sharedPrefEditor.putString(key, value)
            sharedPrefEditor.apply()
        }

        fun getStringFromPreferences(key: String, defaultValue: String, activity: Context?): String? {
            if (activity == null) {
                return defaultValue
            }
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            return sharedPreferences.getString(key, defaultValue)

        }
    }