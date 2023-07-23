package com.diplomproject.view.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import com.diplomproject.databinding.FragmentSettingsBinding
import com.diplomproject.view.root.ViewBindingFragment

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.aboutAppButton.setOnClickListener {
//            getController().openAboutApp()
//        }
    }

    interface Controller {
        fun openAboutApp()
    }

    private fun getController(): Controller = activity as Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}