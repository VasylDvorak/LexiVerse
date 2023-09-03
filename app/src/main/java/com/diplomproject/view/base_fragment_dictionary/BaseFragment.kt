package com.diplomproject.view.base_fragment_dictionary


import android.animation.Animator
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.diplomproject.R
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.navigation.IScreens
import com.diplomproject.utils.ui.AlertDialogFragment
import com.diplomproject.view.AnimatorDictionary
import com.diplomproject.view.OnlineRepository
import com.diplomproject.viewmodel.BaseViewModel
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent

abstract class BaseFragment<T : AppState, B : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean
    ) -> B
) : Fragment(), ViewLayout {
    private var _binding: B? = null

    protected val binding: B
        get() = _binding!!

    private val gson = Gson()

    var mMediaPlayer: MediaPlayer? = null
    private var snack: Snackbar? = null
    protected var isNetworkAvailable: Boolean = false
    private val checkConnection: OnlineRepository by inject()
    abstract val model: BaseViewModel<T>
    protected val checkSDKversion = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val router: Router by KoinJavaComponent.inject(Router::class.java)
    val screen = KoinJavaComponent.getKoin().get<IScreens>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        subscribeToNetworkChange()

    }

    private fun subscribeToNetworkChange() {

        checkConnection.observe(
            this@BaseFragment
        ) {
            isNetworkAvailable = it
            if (!isNetworkAvailable) {
                snack = Snackbar.make(
                    requireView(),
                    R.string.dialog_message_device_is_offline,
                    Snackbar.LENGTH_INDEFINITE
                )
                snack?.show()
            } else {
                snack?.dismiss()
                snack = null
            }
        }
        checkConnection.currentStatus()

    }

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        activity?.let {
            AlertDialogFragment.newInstance(title, message)
                .show(it.supportFragmentManager, DIALOG_FRAGMENT_TAG)
        }
    }

    private fun isDialogNull(): Boolean {
        return activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null
    }


    abstract fun setDataToAdapter(data: List<DataModel>)
    protected open fun renderData(appState: T) {

        when (appState) {
            is AppState.Success -> {
                appState.data?.let {
                    if (it.isEmpty()) {
                        showAlertDialog(
                            getString(R.string.dialog_tittle_sorry),
                            getString(R.string.empty_server_response_on_success)
                        )
                    } else {
                        setDataToAdapter(it)
                    }
                }
            }

            is AppState.Error -> {
                showAlertDialog(
                    getString(R.string.error_stub),
                    appState.error.message
                )
            }
        }
    }

    fun playContentUrl(url: String) {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.apply {
            try {
                setDataSource(url)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setOnPreparedListener { start() }
                prepareAsync()
            } catch (exception: Exception) {
                release()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return AnimatorDictionary().setAnimator(transit, enter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        releaseMediaPlayer()
    }

    companion object {
        const val DIALOG_FRAGMENT_TAG = "dialog_fragment_tag"
    }
}
