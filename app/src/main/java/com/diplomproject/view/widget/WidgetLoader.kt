package com.diplomproject.view.widget

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.preference.PreferenceManager
import com.diplomproject.model.data.DataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

private const val OLD_DATA = "OLD_DATA"
private const val COUNTER = "COUNTER"
const val NEW_DATA = "NEW_DATA"

class WidgetLoader {

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            KoinJavaComponent.getKoin().get()
        )
    }
    private val prefsEditor by lazy { sharedPreferences.edit() }
    private val gson by lazy { Gson() }

    fun updateData(): DataModel? {

        var newData = readData(NEW_DATA)
        if (newData.isNullOrEmpty()) {
            return null
        }
        var oldData = readData(OLD_DATA)
        if (!isEqual(newData, oldData)) {
            saveData(newData, OLD_DATA)
            saveCounter(0)
            oldData = newData
        }
        var outCount = readCounter(COUNTER)
        if ((outCount) >= oldData.size || (oldData.size == 1)) {
            outCount = 0
        }
        saveCounter(outCount + 1)
        return oldData[outCount]
    }


    private fun <T> isEqual(firstList: List<T>, secondList: List<T>): Boolean {

        if (firstList.size != secondList.size) {
            return false
        }
        return gson.toJson(firstList) == gson.toJson(secondList)
    }


    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.apply {
                doInput = true
                connect()
            }
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            null
        }

    }


    fun saveData(oldData: List<DataModel>, callingKey: String) {
        val json = gson.toJson(oldData)
        prefsEditor.apply {
            putString(callingKey, json)
            apply()
        }
    }


    fun readData(callingKey: String): List<DataModel> {
        val type: Type = object : TypeToken<List<DataModel?>?>() {}.type
        val gsonString = sharedPreferences.getString(callingKey, "[]")
        return gson.fromJson(gsonString, type)
    }

    fun readCurrentData(): DataModel? {
        val currentDataList = readData(OLD_DATA)
        if (currentDataList.isNullOrEmpty()) {
            return null
        }
        val currentIndex = readCounter(COUNTER) - 1
        return currentDataList[currentIndex]
    }

    fun saveCounter(counter: Int) {
        prefsEditor.apply {
            putInt(COUNTER, counter)
            apply()
        }
    }

    fun readCounter(callingKey: String): Int {
        return sharedPreferences.getInt(callingKey, 0)
    }
}