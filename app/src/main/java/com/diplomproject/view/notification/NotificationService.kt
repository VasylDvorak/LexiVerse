package com.diplomproject.view.notification

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import com.diplomproject.R
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.view.DictionaryActivity
import com.diplomproject.view.SHOW_DETAILS
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.getKoin
import java.lang.reflect.Type

const val REFERENCE = "REFERENCE"
const val delayFactor = 60000
const val CHANNEL_ID = "channel_id"
const val NOTIFICATION_SETTINGS = "NOTIFICATION_SETTINGS"
class NotificationService : Service() {

    val database = Firebase.database
    var notificatorsCounter = 1
    val myRef = database.getReference(REFERENCE)
    private val gson by lazy { Gson() }


    private var counter = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(getKoin().get())
        val type: Type = object : TypeToken<Pair<Boolean, Int>>() {}.type
        val gsonString = sharedPreferences.getString(NOTIFICATION_SETTINGS, "[]")
        val startServiceNotification = gson.fromJson(gsonString, type)
                as Pair<Boolean, Int>

        if (startServiceNotification.first) {
            myRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val listDataType = object : GenericTypeIndicator<List<DataModel>>() {}
                    val listData = snapshot.getValue(listDataType)
                    updateNotification(listData, startServiceNotification.second)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, getString(R.string.failed_to_read_value_message),
                        error.toException())
                }

            })
        }
        return Service.START_STICKY
    }

    private fun updateNotification(listData: List<DataModel>?, countInterval: Int) {
        val countIntervalLong = (countInterval * delayFactor).toLong()
        listData?.let {
            val timer = object : CountDownTimer(
                countIntervalLong * listData.size,
                countIntervalLong
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    if (counter > (listData.size - 1)) {
                        counter = 0
                    }
                    listData[counter]?.let {
                        showNotification(it)
                    }
                    counter++
                }

                override fun onFinish() {
                    counter = 0
                    updateNotification(listData, countInterval)
                }
            }
            timer.start()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    private fun showNotification(dataModel: DataModel) {
        val notificationIntentStartActivity = Intent(this,
            DictionaryActivity::class.java)


        notificationIntentStartActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        notificationIntentStartActivity.putExtra(SHOW_DETAILS, Gson().toJson(dataModel))
        val pendingIntentToApp = PendingIntent.getActivity(
            this,
            0,
            notificationIntentStartActivity,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val playSoundIntent = Intent(this, PlaySoundService::class.java)

        val appSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putString(PLAY_URL, dataModel.meanings?.get(0)?.soundUrl)
        prefsEditor.apply()


        val pendingIntentPlay = PendingIntent.getService(
            this,
            0,
            playSoundIntent,
            PendingIntent.FLAG_MUTABLE
        )


        val title = dataModel.text +
                "\n[" + dataModel.meanings?.get(0)?.transcription + "]"
        val message = dataModel.meanings?.get(0)?.translation?.translation

        title?.let {
            message?.let {
                val notificationBuilder =
                    Notification.Builder(this, CHANNEL_ID).apply {
                        setSmallIcon(R.drawable.baseline_menu_book_24)
                        setContentTitle(title)
                        setContentText(message)
                        addAction(
                            R.drawable.baseline_volume_up_24, getString(R.string.listen),
                            pendingIntentPlay
                        )
                        addAction(
                            R.drawable.baseline_keyboard_double_arrow_right_24,
                            getString(R.string.to_application),
                            pendingIntentToApp
                        )
                        setAutoCancel(true)
                    }
                var notification = notificationBuilder.build()
                startForeground(notificatorsCounter++, notification)
            }
        }
    }
}
