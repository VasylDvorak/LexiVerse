package com.diplomproject.view.settings_menu

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.diplomproject.R
import com.diplomproject.databinding.FragmentSettingsNotificationBinding
import com.diplomproject.di.ConnectKoinModules
import com.diplomproject.model.datasource.AppState
import com.diplomproject.view.base_for_dictionary.BaseFragmentSettingsMenu
import com.diplomproject.view.favorite.FavoriteViewModel
import com.diplomproject.view.notification.CHANNEL_ID
import com.diplomproject.view.notification.NotificationService

const val minimum = 1
const val maximum = 15
const val timeStepMinutes = 20
private val appVibrationPattern = longArrayOf(500, 500, 500)

class SettingsNotificationFragment : BaseFragmentSettingsMenu<FragmentSettingsNotificationBinding>(
    FragmentSettingsNotificationBinding::inflate
) {

    lateinit var model: FavoriteViewModel
    private val intentService by lazy {
        Intent(
            activity?.applicationContext,
            NotificationService::class.java
        )
    }

    private var notificationOn = false
    private var emptyList = false
    private var setRepeatTime = minimum


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.applicationContext?.stopService(intentService)
        initViewModel()
        if (!emptyList) {
            initiateNotificationSwitch()
        }
    }


    private fun initiateNotificationSwitch() {

        binding.apply {

            switchNotification.apply {
                if (isChecked) {
                    notificationOn = true
                } else {
                    notificationOn = false
                }
                seekBarLayout.apply {
                    if (notificationOn) {
                        visibility = View.VISIBLE
                        initiateSeekBar()
                    } else {
                        visibility = View.GONE
                    }
                }
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {

                        notificationOn = true
                        seekBarLayout.visibility = View.VISIBLE
                        initiateSeekBar()
                    } else {

                        notificationOn = false
                        seekBarLayout.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initiateSeekBar() {
        binding.apply {
            if (notificationOn) {
                textSeekBar.text = getString(R.string.show_notification_every) + " " +
                        (setRepeatTime * timeStepMinutes).toString() + " мин"
                seekBar.apply {
                    max = maximum
                    min = minimum
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                            setRepeatTime = seekBar.progress
                            textSeekBar.text = getString(R.string.show_notification_every) + " " +
                                    (setRepeatTime * timeStepMinutes).toString() + " мин"
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar) {}
                    })
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        if (notificationOn && !emptyList) {
            val pair = Pair(notificationOn && !emptyList,
                setRepeatTime * timeStepMinutes)

            model.setSettingsForNotificator(pair)
            registrateNotification()
            activity?.applicationContext?.startForegroundService(intentService)
        } else {
            activity?.applicationContext?.stopService(intentService)
        }
    }

    fun registrateNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID, getString(R.string.notificator_name),
                importance
            )
            channel.apply {
                vibrationPattern = appVibrationPattern
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun initViewModel() {

        model = ConnectKoinModules.favoriteScreenScope.get()
        binding.apply {
            model.subscribe().observe(viewLifecycleOwner) { appState ->
                when (appState) {
                    is AppState.Success -> {
                        appState.data?.let {
                            if (!it.isNullOrEmpty()) {
                                emptyList = false
                                emptyListText.visibility = View.GONE
                                cardLayout.visibility = View.VISIBLE
                                model.saveInFireBaseDatabase(it)
                            } else {
                                doEmptyList()
                            }
                        }
                    }

                    else -> {
                        doEmptyList()
                    }
                }
            }
        }
        model.getData("", false)
    }

    fun doEmptyList() {
        emptyList = true
        binding.apply {
            emptyListText.visibility = View.VISIBLE
            cardLayout.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance() = SettingsNotificationFragment()
    }
}