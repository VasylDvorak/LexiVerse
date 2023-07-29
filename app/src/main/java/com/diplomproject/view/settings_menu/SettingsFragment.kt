package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diplomproject.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragmentSettingsMenu() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickedViews()
    }

    private fun initClickedViews() {
        binding.apply {
            with(router) {
                screen.apply {
                    registration.setOnClickListener { navigateTo(startRegistrationFragment()) }
                    settingsNotification.setOnClickListener {
                        navigateTo(startSettingsNotificationFragment())
                    }
                    support.setOnClickListener { navigateTo(startSupportFragment()) }
                    choosingTheme.setOnClickListener { navigateTo(startChoosingThemeFragment()) }
                    shareApplication.setOnClickListener {
                        navigateTo(startShareApplicationFragment())
                    }
                    enterExit.setOnClickListener { navigateTo(startEnterExitFragment()) }
                    aboutApplication.setOnClickListener {
                        navigateTo(startAboutApplicationFragment()
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() =SettingsFragment()

    }
}