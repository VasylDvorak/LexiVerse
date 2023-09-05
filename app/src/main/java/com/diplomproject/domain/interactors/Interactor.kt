package com.diplomproject.domain.interactors


import android.media.AudioManager
import android.media.MediaPlayer
import com.diplomproject.model.data_word_request.DataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

interface Interactor<T : Any> {

    var mMediaPlayer: MediaPlayer?
    suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<T>
    suspend fun getWordFromDB(word: String): Flow<DataModel> = flow {}
    suspend fun putFavorite(favorite: DataModel) {}
    suspend fun removeFavoriteItem(removeFavorite: DataModel) {}
    fun playContentUrl( url: String) {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.apply {
            try {
                setDataSource(url)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setOnPreparedListener { start() }
                prepareAsync()
            } catch (exception: IOException) {
                release()
                null
            }
        }
    }

    fun releaseMediaPlayer(){
        mMediaPlayer?.apply {
            if (isPlaying == true) {
                stop()
                release()
                mMediaPlayer = null
            }
        }
    }

}