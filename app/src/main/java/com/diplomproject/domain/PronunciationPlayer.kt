package com.diplomproject.domain

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.CountDownTimer
import java.io.IOException

private const val COUNT_DOWN_INTERVAL = 100L

class PronunciationPlayer(private val audioManager: AudioManager) {
    var mMediaPlayer: MediaPlayer? = null
    fun playContentUrl(url: String) {

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

            val lengthPlaying = mMediaPlayer!!.duration.toLong()
            val timerMute = object : CountDownTimer(lengthPlaying, COUNT_DOWN_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)
                    val timer = object : CountDownTimer(lengthPlaying, COUNT_DOWN_INTERVAL) {
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
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }

}