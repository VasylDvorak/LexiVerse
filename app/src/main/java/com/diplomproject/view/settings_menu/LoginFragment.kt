package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.diplomproject.R
import com.diplomproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragmentSettingsMenu<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
)  {

    var auth: FirebaseAuth? = null
    private val MAX_CHARACTERS = 30


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        initClickedViews()
        initView()
    }

    private fun initClickedViews() {
        binding.apply {
            buttonLogin.setOnClickListener{
                loginUser()
            }

        }
    }

    private fun initView(){
        setCharRestriction(binding.editTextTextEmailAddress)
        setCharRestriction(binding.editTextNumberPassword)
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

    private fun setCharRestriction(editText: AppCompatEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > MAX_CHARACTERS) {
                    val truncatedText = charSequence.toString().substring(0, MAX_CHARACTERS)
                    editText.setText(truncatedText)
                    editText.setSelection(MAX_CHARACTERS)
                    Toast.makeText(requireContext(), getString(R.string.restriction_text),
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(editable: Editable) {     }
        })

    }


    companion object {
        fun newInstance() = LoginFragment()

    }
}