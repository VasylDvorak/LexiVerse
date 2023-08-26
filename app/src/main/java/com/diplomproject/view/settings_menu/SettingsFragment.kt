package com.diplomproject.view.settings_menu

import android.os.Bundle
import android.view.View
import com.diplomproject.R
import com.diplomproject.databinding.FragmentSettingsBinding
import com.diplomproject.view.root.ViewBindingFragment
import com.diplomproject.view.settings_menu.authorized_fragments.AuthorizedFragment
import com.diplomproject.view.settings_menu.authorized_fragments.NotAuthorizedFragment
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {
    var auth: FirebaseAuth? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loadAuthorizedFragment()
        initClickedViews()

    }

    private fun initClickedViews() {
        binding.apply {
            with(router) {
                screen.apply {

                    settingsNotification.setOnClickListener {
                        navigateTo(startSettingsNotificationFragment())
                    }

                    support.setOnClickListener {
                        navigateTo(startSupportFragment())
                    }


                    shareApplication.setOnClickListener {
                        navigateTo(startShareApplicationFragment())
                    }

                    aboutApplication.setOnClickListener {
                        navigateTo(startAboutApplicationFragment())
                    }
                }
            }
        }
    }

    private fun loadAuthorizedFragment(){
        if(auth?.currentUser != null) {// если пользователь авторизован, то загружаем первый вариант подфрагмента, если нет, то второй
            fragmentManager?.beginTransaction()?.replace(R.id.container_authorized, AuthorizedFragment(auth?.currentUser?.email ?: ""))?.commit()
        }else{
            fragmentManager?.beginTransaction()?.replace(R.id.container_authorized, NotAuthorizedFragment())?.commit()
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}