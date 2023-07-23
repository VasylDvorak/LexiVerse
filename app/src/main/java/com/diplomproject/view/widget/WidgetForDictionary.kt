package com.diplomproject.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import com.diplomproject.R
import com.diplomproject.model.data.DataModel
import com.diplomproject.view.MainActivity
import com.diplomproject.view.SHOW_DETAILS
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

private const val CHANGE_WORD = "CHANGE_WORD"
private const val PLAY_PRONUNCIATION = "PLAY_PRONUNCIATION"
private const val START_MAIN_ACTIVITY = "START_MAIN_ACTIVITY"

class WidgetForDictionary : AppWidgetProvider() {

    var mMediaPlayer: MediaPlayer? = null

    private val widgetLoader by lazy { WidgetLoader() }


    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            CHANGE_WORD -> {
                receiveData(context, false)
            }

            PLAY_PRONUNCIATION -> {
                widgetLoader.readCurrentData()
                    ?.let { playContentUrl(it.meanings?.get(0)?.soundUrl) }
            }

            START_MAIN_ACTIVITY -> {
                widgetLoader.readCurrentData()?.let {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra(SHOW_DETAILS, Gson().toJson(it))
                    context.startActivity(intent)
                }
            }

            else -> {
                return
            }
        }
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        receiveData(context, true)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        receiveData(context, true)
    }


    private fun receiveData(
        context: Context, withPendingIntent: Boolean
    ) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetId = ComponentName(context, WidgetForDictionary::class.java)

        RemoteViews(
            context.packageName, R.layout.widget_layout
        ).apply {
            scope.launch {
                if (withPendingIntent) {

                    val pendingIntentChangeText = createPendingIntend(CHANGE_WORD, context)
                    val pendingIntentPlayPronunciation =
                        createPendingIntend(PLAY_PRONUNCIATION, context)
                    val pendingIntentStartMainActivity =
                        createPendingIntend(START_MAIN_ACTIVITY, context)

                    setOnClickPendingIntent(R.id.text_unit_layout_widget, pendingIntentChangeText)
                    setOnClickPendingIntent(R.id.header_textview_widget, pendingIntentChangeText)
                    setOnClickPendingIntent(
                        R.id.play_articulation_widget,
                        pendingIntentPlayPronunciation
                    )
                    setOnClickPendingIntent(
                        R.id.start_main_activity,
                        pendingIntentStartMainActivity
                    )
                }
                loadLayout(widgetLoader.updateData(), this@apply, context)
                appWidgetManager.updateAppWidget(appWidgetId, this@apply)
            }

        }
    }

    fun createPendingIntend(actionName: String, context: Context) =
        Intent(context, WidgetForDictionary::class.java).let { intent ->
            intent.action = actionName
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

    fun loadLayout(showData: DataModel?, remoteViews: RemoteViews, context: Context) {
        remoteViews.apply {
            if (showData != null) {
                setViewVisibility(R.id.play_articulation_widget, View.VISIBLE)
                setTextViewText(R.id.header_textview_widget, showData.text)
                setTextViewText(
                    R.id.description_textview_widget,
                    showData.meanings?.get(0)?.translation?.translation
                )
                setTextViewText(
                    R.id.transcription_textview_widget,
                    "[" + showData.meanings?.get(0)?.transcription + "]"
                )
            } else {
                setTextViewText(
                    R.id.header_textview_widget,
                    context.getString(R.string.empty_list_for_widget)
                )
                setTextViewText(R.id.description_textview_widget, "")
                setTextViewText(R.id.transcription_textview_widget, "")
                setViewVisibility(R.id.play_articulation_widget, View.INVISIBLE)
            }
        }
    }

    override fun onDisabled(context: Context?) {
        releaseMediaPlayer()
        super.onDisabled(context)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        releaseMediaPlayer()
        super.onDeleted(context, appWidgetIds)
    }

    fun playContentUrl(url: String?) {
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

    fun releaseMediaPlayer() {
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }


}


