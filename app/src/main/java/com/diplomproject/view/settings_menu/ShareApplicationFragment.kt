package com.diplomproject.view.settings_menu

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.diplomproject.R
import com.diplomproject.databinding.FragmentShareBinding


class ShareApplicationFragment : BaseFragmentSettingsMenu<FragmentShareBinding>(
    FragmentShareBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickedViews()
    }

    private fun initClickedViews() {
        binding.apply {
            copyButton.setOnClickListener {
                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Label", getString(R.string.download_link))
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(context, getString(R.string.copy_toast), Toast.LENGTH_LONG)
                    .show()
            }

            imageViewShareLink.setOnClickListener {
                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Label",
                    getString(R.string.download_link))
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(context, getString(R.string.copy_toast), Toast.LENGTH_LONG)
                    .show()
            }

            sendButton.setOnClickListener {
                shareApp()
            }
        }
    }

    private fun shareApp() {
        val appUrl = getString(R.string.download_link)
        val messageForApp = getString(R.string.message_for_share_app)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$messageForApp $appUrl")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Поделиться приложением")
        startActivity(shareIntent)
    }


    companion object {
        fun newInstance() = ShareApplicationFragment()

    }
}