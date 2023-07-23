package com.diplomproject.view.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.diplomproject.R
import com.diplomproject.databinding.FragmentAboutAppBinding
import com.diplomproject.view.root.ViewBindingFragment
import com.google.android.apps.common.testing.accessibility.framework.BuildConfig

class AboutAppFragment : ViewBindingFragment<FragmentAboutAppBinding>(
    FragmentAboutAppBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        informationApp()
    }

    @SuppressLint("SetTextI18n")
    private fun informationApp() {
        binding.codVersionTextView.text =
            getString(R.string.version_code) + BuildConfig.VERSION_CODE
        binding.versionTextView.text = getString(R.string.version_name) + BuildConfig.VERSION_NAME
        binding.aboutAppTextView.text = getString(R.string.app_info)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AboutAppFragment()
    }
}