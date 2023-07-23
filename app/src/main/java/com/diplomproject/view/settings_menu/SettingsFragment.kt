package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diplomproject.R
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
        activity?.title = getString(R.string.title_settings)
        initClickedViews()
    }

    private fun initClickedViews() {
        binding.apply {
            with(router) {
                screen.apply {
                    registration.setOnClickListener { replaceScreen(startRegistrationFragment()) }
                    settingsNotification.setOnClickListener {
                        replaceScreen(
                            startSettingsNotificationFragment()
                        )
                    }
                    support.setOnClickListener { replaceScreen(startSupportFragment()) }
                    choosingTheme.setOnClickListener { replaceScreen(startChoosingThemeFragment()) }
                    shareApplication.setOnClickListener {
                        replaceScreen(
                            startShareApplicationFragment()
                        )
                    }
                    enterExit.setOnClickListener { replaceScreen(startEnterExitFragment()) }
                    aboutApplication.setOnClickListener {
                        replaceScreen(
                            startAboutApplicationFragment()
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
        fun newInstance() = SettingsFragment()

    }
}