package com.diplomproject.view.settings_menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.diplomproject.R
import com.diplomproject.databinding.FragmentShareBinding


class ShareApplicationFragment : BaseFragmentSettingsMenu() {

    private var _binding: FragmentShareBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                        smsManager.sendTextMessage(phoneNumber, null, message,
                            null, null)
                        Toast.makeText(context, getString(R.string.message_were_sent),
                            Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, getString(R.string.enter_all_data), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ShareApplicationFragment()

    }
}