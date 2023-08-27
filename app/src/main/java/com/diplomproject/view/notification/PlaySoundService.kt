package com.diplomproject.view.notification

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder
import android.preference.PreferenceManager
import org.koin.java.KoinJavaComponent
import java.io.IOException

const val PLAY_URL = "PLAY_URL"
private const val COUNT_DOWN_INTERVAL = 100L

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
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun playContentUrl(url: String) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mMediaPlayer = MediaPlayer()
        if (audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
            mMediaPlayer = null
        } else {
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

            val timerMute = object : CountDownTimer(
                mMediaPlayer!!.duration.toLong(),
                COUNT_DOWN_INTERVAL
            ) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)
                    val timer = object : CountDownTimer(
                        mMediaPlayer!!.duration.toLong(),
                        COUNT_DOWN_INTERVAL
                    ) {
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false)
                        }
                    }
                    timer.start()
                }
            }
            timerMute.start()
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