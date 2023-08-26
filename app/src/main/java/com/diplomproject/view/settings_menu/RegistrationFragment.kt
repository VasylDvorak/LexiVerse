package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.diplomproject.R
import com.diplomproject.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationFragment : BaseFragmentSettingsMenu<FragmentRegistrationBinding>(
    FragmentRegistrationBinding::inflate
)  {

    var auth: FirebaseAuth? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        initClickedViews()
    }

    private fun initClickedViews() {
        binding.apply {
            button.setOnClickListener {
                createUser()
            }
            privacyTextView.setOnClickListener{
                router.navigateTo(screen.startPoliticFragment())
            }
        }
    }


    private fun createUser(){

            val email: String = binding?.editTextTextEmailAddress?.getText().toString()
            val password: String = binding?.editTextNumberPassword?.getText().toString()
           // val password: String = binding?.editTextNumberPassword2?.text

            if (TextUtils.isEmpty(email)) {
                binding?.editTextTextEmailAddress?.setError(getString(R.string.error_email_empty))
                binding?.editTextTextEmailAddress?.requestFocus()
            } else if (TextUtils.isEmpty(password)) {
                binding?.editTextNumberPassword?.setError(getString(R.string.error_password_empty))
                binding?.editTextNumberPassword?.requestFocus()
            } else if (!password.equals(binding?.editTextNumberPassword2?.text?.toString())) {
                binding?.editTextNumberPassword2?.setError(getString(R.string.error_match_password))
                binding?.editTextNumberPassword2?.requestFocus()
            } else if (binding?.privacyCheckBox?.isChecked != true){
                binding?.privacyCheckBox?.setBackgroundColor(resources.getColor(R.color.color_error) )
                Toast.makeText(
                    this@RegistrationFragment.context,
                    getString(R.string.confirm_polycy),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@RegistrationFragment.context,
                            getString(R.string.registration_succesful),
                            Toast.LENGTH_SHORT
                        ).show()
                        router.navigateTo(screen.startSettingsFragment())
                    } else {
                        Toast.makeText(
                            this@RegistrationFragment.context,
                            getString(R.string.fail_registration) + task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }



    }

    companion object {
        fun newInstance() = RegistrationFragment()

    }
}