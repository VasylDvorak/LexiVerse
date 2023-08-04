package com.diplomproject.view

import android.animation.ObjectAnimator

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.diplomproject.R
import com.diplomproject.databinding.ActivityMainBinding
import com.diplomproject.di.ConnectKoinModules
import com.diplomproject.model.data_word_request.AppState
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.navigation.IScreens
import com.diplomproject.view.favorite.FavoriteViewModel
import com.diplomproject.view.widget.NEW_DATA
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent


private const val DURATION = 1000L
private const val COUNTDOWN_DURATION = 2000L
private const val COUNTDOWN_INTERVAL = 1000L
const val SHOW_DETAILS = "SHOW_DETAILS"

class DictionaryActivity : AppCompatActivity() {
    private val gson = Gson()
    private val navigatorHolder: NavigatorHolder by inject()
    private val router: Router by KoinJavaComponent.inject(Router::class.java)
    private val screen = KoinJavaComponent.getKoin().get<IScreens>()
    val navigator = AppNavigator(this, R.id.container)
    lateinit var model: FavoriteViewModel

    private var vb: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultSplashScreen()
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb?.root)

        val fromWidget = intent.extras?.getString(SHOW_DETAILS, null)
        val showDataModel = Gson().fromJson(fromWidget, DataModel::class.java)
        if (showDataModel != null) {
            router.replaceScreen(screen.startDescriptionFragment(showDataModel))
        } else {
            router.replaceScreen(screen.startMainFragment())
        }
    }

    private fun initViewModel() {

        val viewModel: FavoriteViewModel by lazy { ConnectKoinModules.favoriteScreenScope.get() }
        model = viewModel

        model.subscribe().observe(this) { appState ->
            when (appState) {
                is AppState.Success -> {
                    appState.data?.let {

                        val appSharedPrefs =
                            PreferenceManager.getDefaultSharedPreferences(applicationContext)
                        val prefsEditor = appSharedPrefs.edit()

                        val json = gson.toJson(it)
                        prefsEditor.putString(NEW_DATA, json)
                        prefsEditor.apply()
                    }
                }

                else -> {}
            }
        }
        model.getData("", false)
    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        initViewModel()
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    private fun setDefaultSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //   setSplashScreenHideAnimation()
            setSplashScreenDuration()
        }
    }

    @RequiresApi(31)
    private fun setSplashScreenHideAnimation() {

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            ).apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = DURATION
                doOnEnd {
                    splashScreenView.removeAllViews()
                }
                start()
            }
        }

    }

    private fun setSplashScreenDuration() {
        var isHideSplashScreen = false
        object : CountDownTimer(COUNTDOWN_DURATION, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                isHideSplashScreen = true
            }
        }.start()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isHideSplashScreen) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

    }

}