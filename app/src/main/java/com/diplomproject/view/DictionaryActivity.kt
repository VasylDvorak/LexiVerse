package com.diplomproject.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.diplomproject.R
import com.diplomproject.databinding.ActivityMainBinding
import com.diplomproject.di.ConnectKoinModules
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.navigation.IScreens
import com.diplomproject.utils.delegates.SharedPreferencesDelegate
import com.diplomproject.view.favorite.FavoriteViewModel
import com.diplomproject.view.widget.NEW_DATA
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent


const val SHOW_DETAILS = "SHOW_DETAILS"

class DictionaryActivity : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private val navigatorHolder: NavigatorHolder by inject()
    private val router: Router by KoinJavaComponent.inject(Router::class.java)
    private val screen = KoinJavaComponent.getKoin().get<IScreens>()
    val navigator = AppNavigator(this, R.id.container)
    lateinit var model: FavoriteViewModel

    private var vb: ActivityMainBinding? = null
    public override fun onStart(){
        super.onStart()
        auth.currentUser
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        auth = Firebase.auth
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
                        var listFromSharedPreferences: List<DataModel> by
                        SharedPreferencesDelegate(NEW_DATA)
                        listFromSharedPreferences = it
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
}

class OnlineRepository : LiveData<Boolean>() {

    private val context: Context = KoinJavaComponent.getKoin().get()
    private val availableNetworks = mutableSetOf<Network>()
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val request: NetworkRequest = NetworkRequest.Builder().build()
    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            availableNetworks.remove(network)
            update(availableNetworks.isNotEmpty())
        }

        override fun onAvailable(network: Network) {
            availableNetworks.add(network)
            update(availableNetworks.isNotEmpty())
        }
    }


    override fun onActive() {
        connectivityManager.registerNetworkCallback(request, callback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

    private fun update(online: Boolean) {
        if (online != value) {
            postValue(online)
        }
    }

    fun currentStatus(): Boolean {
        onActive()
        val hasNetwork = availableNetworks.isNotEmpty()
        postValue(hasNetwork)
        return hasNetwork
    }
}