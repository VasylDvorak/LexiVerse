package com.diplomproject.view.settings_menu

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
            sendSMS.setOnClickListener {
                val phoneNumber = enterPhoneNumber.text.toString()
                val message = getString(R.string.for_sms) + webLink.text.toString()
                try {
                    val smsManager: SmsManager
                    if (Build.VERSION.SDK_INT >= 23) {
                        smsManager = requireActivity().getSystemService(SmsManager::class.java)
                    } else {
                        smsManager = SmsManager.getDefault()
                    }
                    if (ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.SEND_SMS
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.SEND_SMS),
                            1
                        );
                    } else {
                        smsManager.sendTextMessage(
                            phoneNumber, null, message,
                            null, null
                        )
                        Toast.makeText(
                            context, getString(R.string.message_were_sent),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, getString(R.string.enter_all_data), Toast.LENGTH_LONG)
                        .show()
                }
            }
            copyButton.setOnClickListener {
                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Label", getString(R.string.download_link))
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(context, getString(R.string.copy_toast), Toast.LENGTH_LONG)
                    .show()
            }

            imageViewShare2.setOnClickListener {
                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Label", getString(R.string.download_link))
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