package com.example.konturtest

import android.content.Context
import androidx.preference.PreferenceManager

object LastRequestSharedPreference {

    val getContactsKey = "GET_CONTACTS"

    fun getLastTimeForRequest(context: Context, key: String): Long {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getLong(key, Long.MAX_VALUE)
    }

    fun setTimeByRequest(context: Context, key : String, time: Long) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putLong(key, time)
            .apply()
    }
}