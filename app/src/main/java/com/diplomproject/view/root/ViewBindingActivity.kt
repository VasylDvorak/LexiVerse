package com.diplomproject.view.root

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewbinding.ViewBinding
import com.diplomproject.model.splash_fetching_data.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

open class ViewBindingActivity<T : ViewBinding>(
    private val inflateBinding: (inflater: LayoutInflater) -> T
) : AppCompatActivity() {
    private var _binding: T? = null
    private lateinit var auth: FirebaseAuth
    private val viewModel: SplashViewModel by viewModels()
    protected val binding: T
        get() = _binding!!
    public override fun onStart(){
        super.onStart()
        auth.currentUser
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        //SplashScreen
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }

        _binding = inflateBinding.invoke(layoutInflater)

        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}