package com.diplomproject.learningtogether

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

open class ViewBindingActivity<T : ViewBinding>(
    private val inflateBinding: (inflater: LayoutInflater) -> T
) : AppCompatActivity() {
    private var _binding: T? = null

    protected val binding: T
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding.invoke(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}