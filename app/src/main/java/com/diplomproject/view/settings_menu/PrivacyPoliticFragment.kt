package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.text.Html
import android.view.View
import com.diplomproject.R
import com.diplomproject.databinding.FragmentPrivacyPoliticBinding


class PrivacyPoliticFragment : BaseFragmentSettingsMenu<FragmentPrivacyPoliticBinding>(
    FragmentPrivacyPoliticBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHtmlText()
    }

    private fun setHtmlText() {
        val policyHtmlText = getString(R.string.privacy_policy_and_conditions_eng_text)
        binding.policyTextView.text = Html.fromHtml(policyHtmlText, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun initClickedViews() {

    }


    companion object {
        fun newInstance() = PrivacyPoliticFragment()

    }
}