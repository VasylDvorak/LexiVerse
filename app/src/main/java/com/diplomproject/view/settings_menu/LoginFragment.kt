package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.diplomproject.R
import com.diplomproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragmentSettingsMenu<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
)  {

    var auth: FirebaseAuth? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        initClickedViews()
    }

    private fun initClickedViews() {
        binding.apply {
            buttonLogin.setOnClickListener{
                loginUser()
            }

        }
    }

    private fun loginUser() {
        val email: String = binding?.editTextTextEmailAddress?.getText().toString()
        val password: String = binding?.editTextNumberPassword?.getText().toString()
        if (TextUtils.isEmpty(email)) {
            binding?.editTextTextEmailAddress?.setError(getString(R.string.error_email_empty))
            binding?.editTextTextEmailAddress?.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            binding?.editTextNumberPassword?.setError(getString(R.string.error_password_empty))
            binding?.editTextNumberPassword?.requestFocus()
        } else {
            auth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@LoginFragment.context,
                        getString(R.string.succesful_login),
                        Toast.LENGTH_SHORT
                    ).show()
                    router.navigateTo(screen.startSettingsFragment())
                } else {
                    Toast.makeText(
                        this@LoginFragment.context,
                        getString(R.string.fail_login) + task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    companion object {
        fun newInstance() = LoginFragment()

    }
}