package com.diplomproject.utils.delegates

import android.content.Context
import android.preference.PreferenceManager
import com.diplomproject.model.data_word_request.DataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.mp.KoinPlatform.getKoin
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferencesDelegate(private val name: String) :
    ReadWriteProperty<Any?, List<DataModel>?> {
    private val appSharedPrefs =
        PreferenceManager.getDefaultSharedPreferences(getKoin().get<Context>().applicationContext)
    private val gson by lazy { Gson() }

    override fun getValue(thisRef: Any?, property: KProperty<*>): List<DataModel> {
        val type: Type = object : TypeToken<List<DataModel?>?>() {}.type
        val gsonString = appSharedPrefs.getString(name, "[]")
        return gson.fromJson(gsonString, type)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<DataModel>?) {
        val prefsEditor = appSharedPrefs.edit()
        val jsonStr = gson.toJson(value)
        prefsEditor.putString(name, jsonStr)
        prefsEditor.apply()
    }
}