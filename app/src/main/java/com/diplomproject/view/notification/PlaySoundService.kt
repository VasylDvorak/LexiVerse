package com.diplomproject.view.notification

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import android.preference.PreferenceManager
import com.diplomproject.domain.PronunciationPlayer
import org.koin.java.KoinJavaComponent

const val PLAY_URL = "PLAY_URL"

class PlaySoundService : Service() {

    val pronunciationPlayer by lazy {
        PronunciationPlayer(
            getSystemService(AUDIO_SERVICE)
                    as AudioManager
        )
    }

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            KoinJavaComponent.getKoin().get()
        )
    }

    fun readData(callingKey: String) = sharedPreferences.getString(callingKey, null)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        readData(PLAY_URL)?.let { pronunciationPlayer.playContentUrl(it) }
        onDestroy()
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onDestroy() {
        pronunciationPlayer.releaseMediaPlayer()
        super.onDestroy()
    }

}