package com.diplomproject.domain.interactors

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.diplomproject.R
import com.diplomproject.model.data_description_request.DescriptionAppState
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.model.data_word_request.Meanings
import com.diplomproject.model.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.mp.KoinPlatform.getKoin
import java.io.IOException

class DescriptionInteractor(
    var repositoryRemote: Repository<List<Example>>,
) {
    var mMediaPlayer: MediaPlayer? = null
    suspend fun getData(
        meanings: List<Meanings>,
        fromRemoteSource: Boolean
    ): StateFlow<DescriptionAppState> {
        val descriptionAppState: DescriptionAppState
        if (fromRemoteSource) {
            descriptionAppState =
                DescriptionAppState.Success(repositoryRemote.getDataDescription(meanings))
        } else {
            val context = getKoin().get<Context>()
            descriptionAppState = DescriptionAppState.Error(
                Throwable(
                    context.getString(R.string.no_connection_internet)
                )
            )
        }
        return MutableStateFlow(descriptionAppState)
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

