package com.diplomproject.view.settings_menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.diplomproject.BuildConfig
import com.diplomproject.R
import com.diplomproject.databinding.FragmentAboutApplicationBinding



class AboutApplicationFragment : BaseFragmentSettingsMenu<FragmentAboutApplicationBinding>(
    FragmentAboutApplicationBinding::inflate
) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickedViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initClickedViews() {
        binding.apply {
            codVersionTextView.text = getString(R.string.version_code) + BuildConfig.VERSION_CODE
            versionTextView.text = getString(R.string.version_name) + BuildConfig.VERSION_NAME
            aboutAppTextView.text = getString(R.string.app_info)
        }
    }

    companion object {
        fun newInstance() = AboutApplicationFragment()

    }
}