package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.view.View
import com.diplomproject.databinding.FragmentSupportBinding


class SupportFragment : BaseFragmentSettingsMenu<FragmentSupportBinding>(
    FragmentSupportBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickedViews()
    }

    private fun initClickedViews() {
        binding.apply {

        }
    }

    companion object {
        fun newInstance() = SupportFragment()

    }
}