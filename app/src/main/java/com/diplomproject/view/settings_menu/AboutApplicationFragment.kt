package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diplomproject.R
import com.diplomproject.databinding.FragmentAboutApplicationBinding
import com.google.android.apps.common.testing.accessibility.framework.BuildConfig


class AboutApplicationFragment : BaseFragmentSettingsMenu() {

    private var _binding: FragmentAboutApplicationBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutApplicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickedViews()
    }

    private fun initClickedViews() {
        binding.apply {
            codVersionTextView.text = getString(R.string.version_code) + BuildConfig.VERSION_CODE
            versionTextView.text = getString(R.string.version_name) + BuildConfig.VERSION_NAME
            aboutAppTextView.text = getString(R.string.app_info)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = AboutApplicationFragment()

    }
}