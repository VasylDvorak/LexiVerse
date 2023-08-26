package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.view.View
import com.diplomproject.databinding.FragmentSettingsBinding
import com.diplomproject.view.root.ViewBindingFragment

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickedViews()
    }

    private fun initClickedViews() {
        binding.apply {
            with(router) {
                screen.apply {

                    registration.setOnClickListener {
                        navigateTo(startRegistrationFragment())
                    }

                    login.setOnClickListener{
                        navigateTo(startLoginFragment())
                    }

                    settingsNotification.setOnClickListener {
                        navigateTo(startSettingsNotificationFragment())
                    }

                    support.setOnClickListener {
                        navigateTo(startSupportFragment())
                    }


                    shareApplication.setOnClickListener {
                        navigateTo(startShareApplicationFragment())
                    }

                    enterExit.setOnClickListener {
                        navigateTo(startEnterExitFragment())
                    }

                    aboutApplication.setOnClickListener {
                        navigateTo(startAboutApplicationFragment())
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}