package com.diplomproject.learningtogether

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewbinding.ViewBinding
import com.diplomproject.learningtogether.model.splash_fetching_data.LearningSplashViewModel

open class ViewBindingActivity<T : ViewBinding>(
    private val inflateBinding: (inflater: LayoutInflater) -> T) : AppCompatActivity() {
    private var _binding: T? = null

    private val viewModel: LearningSplashViewModel by viewModels()

    protected val binding: T
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //SplashScreen
        installSplashScreen().apply {
            setKeepOnScreenCondition{
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