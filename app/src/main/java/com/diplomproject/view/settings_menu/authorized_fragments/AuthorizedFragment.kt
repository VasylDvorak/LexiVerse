package com.diplomproject.view.settings_menu.authorized_fragments

import android.os.Bundle
import android.view.View
import com.diplomproject.databinding.FragmentAuthorizedBinding
import com.diplomproject.utils.ui.ExitDialog
import com.diplomproject.view.root.ViewBindingFragment
import com.google.firebase.auth.FirebaseAuth


class AuthorizedFragment(private val userEmail: String):
    ViewBindingFragment<FragmentAuthorizedBinding> (FragmentAuthorizedBinding::inflate) {
    var auth: FirebaseAuth? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        initViews()
        initClickedViews()

    }



    private fun initClickedViews() {
        binding.apply {
            with(router) {
                screen.apply {
                    enterExit.setOnClickListener {
                        ExitDialog.showExitDialog(requireContext(), auth, router, screen)
                    }
                }
            }
        }
    }

        private fun initViews() {
            binding.welcomeText.text = userEmail
        }
    }
