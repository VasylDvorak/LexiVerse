package com.diplomproject.navigation

import android.os.Bundle
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.view.description.DescriptionFragment
import com.diplomproject.view.favorite.FavoriteFragment
import com.diplomproject.view.main_fragment.MainFragment
import com.diplomproject.view.history.HistoryFragment
import com.diplomproject.view.settings_menu.AboutApplicationFragment
import com.diplomproject.view.settings_menu.ChoosingThemeFragment
import com.diplomproject.view.settings_menu.EnterExitFragment
import com.diplomproject.view.settings_menu.RegistrationFragment
import com.diplomproject.view.settings_menu.SettingsFragment
import com.diplomproject.view.settings_menu.SettingsNotificationFragment
import com.diplomproject.view.settings_menu.ShareApplicationFragment
import com.diplomproject.view.settings_menu.SupportFragment

class AndroidScreens : IScreens {
    override fun startMainFragment() = FragmentScreen { MainFragment.newInstance() }

    override fun startDescriptionFragment(dataModel: DataModel): Screen = FragmentScreen {
        DescriptionFragment.newInstance(Bundle().apply {
            putParcelable(
                DescriptionFragment.CURRENT_DATA_MODEl,
                dataModel
            )
        })
    }

    override fun startHistoryFragment() = FragmentScreen { HistoryFragment.newInstance() }
    override fun startFavoriteFragment() = FragmentScreen { FavoriteFragment.newInstance() }
    override fun startSettingsFragment() = FragmentScreen { SettingsFragment.newInstance() }
    override fun startRegistrationFragment() = FragmentScreen { RegistrationFragment.newInstance() }
    override fun startSettingsNotificationFragment() =
        FragmentScreen { SettingsNotificationFragment.newInstance() }

    override fun startSupportFragment() = FragmentScreen { SupportFragment.newInstance() }
    override fun startChoosingThemeFragment() =
        FragmentScreen { ChoosingThemeFragment.newInstance() }

    override fun startShareApplicationFragment() =
        FragmentScreen { ShareApplicationFragment.newInstance() }

    override fun startEnterExitFragment() = FragmentScreen { EnterExitFragment.newInstance() }
    override fun startAboutApplicationFragment() =
        FragmentScreen { AboutApplicationFragment.newInstance() }

}



