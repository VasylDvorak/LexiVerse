package com.diplomproject.view.notification

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.preference.PreferenceManager
import org.koin.java.KoinJavaComponent
import java.io.IOException

const val PLAY_URL = "PLAY_URL"

class PlaySoundService : Service() {
    var mMediaPlayer: MediaPlayer? = null

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            KoinJavaComponent.getKoin().get()
        )
    }

    fun readData(callingKey: String) = sharedPreferences.getString(callingKey, null)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        readData(PLAY_URL)?.let { playContentUrl(it) }
        onDestroy()
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun playContentUrl(url: String) {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.apply {
            try {
                setDataSource(url)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setOnPreparedListener { start() }
                prepareAsync()
            } catch (exception: IOException) {
                releaseMediaPlayer()
                null
            }
        }
    }

    fun releaseMediaPlayer() {
        mMediaPlayer?.apply {
            if (isPlaying == true) {
                stop()
                release()
                mMediaPlayer = null
            }
        }
    }

    override fun onDestroy() {
        releaseMediaPlayer()
        super.onDestroy()
    }

}