package com.diplomproject.view.widget

import android.content.Context
import android.media.AudioManager
import android.preference.PreferenceManager
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.utils.PronunciationPlayer
import com.diplomproject.utils.network.SharedPreferencesDelegate
import com.google.gson.Gson
import org.koin.java.KoinJavaComponent
import org.koin.mp.KoinPlatform.getKoin

private const val OLD_DATA = "OLD_DATA"
private const val COUNTER = "COUNTER"
const val NEW_DATA = "NEW_DATA"

class WidgetLoader {

    val pronunciationPlayer by lazy {
        PronunciationPlayer(
            getKoin().get<Context>().applicationContext
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        )
    }

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            KoinJavaComponent.getKoin().get()
        )
    }
    private val prefsEditor by lazy { sharedPreferences.edit() }
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
        val gson = Gson()
        return gson.toJson(firstList) == gson.toJson(secondList)
    }

    fun saveData(oldData: List<DataModel>, callingKey: String) {
        var listFromSharedPreferences: List<DataModel> by SharedPreferencesDelegate(callingKey)
        listFromSharedPreferences = oldData
    }


    fun readData(callingKey: String): List<DataModel> {
        var listFromSharedPreferences: List<DataModel> by SharedPreferencesDelegate(callingKey)
        return listFromSharedPreferences
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

    fun playContentUrl(url: String) {
        pronunciationPlayer.playContentUrl(url)
    }

    fun releaseMediaPlayer() {
        pronunciationPlayer.releaseMediaPlayer()
    }
}