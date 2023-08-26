package com.diplomproject.view.settings_menu.authorized_fragments

import android.os.Bundle
import android.view.View
import com.diplomproject.databinding.FragmentNotAuthorizedBinding
import com.diplomproject.view.root.ViewBindingFragment


class NotAuthorizedFragment : ViewBindingFragment<FragmentNotAuthorizedBinding>(
    FragmentNotAuthorizedBinding::inflate
) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickedViews()

    }

    private fun initClickedViews() {
        binding.apply {
            with(router) {
                screen.apply {
                    login.setOnClickListener {
                        navigateTo(startLoginFragment())
                    }
                    registration.setOnClickListener {
                        navigateTo(startRegistrationFragment())
                    }
                }
            }
        }
    }
}