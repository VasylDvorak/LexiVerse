package com.diplomproject.navigation

import android.os.Bundle
import com.diplomproject.learningtogether.ui.favorites.FavouritesFragment
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.view.description.CURRENT_DATA_MODEl
import com.diplomproject.view.description.DescriptionFragment
import com.diplomproject.view.favorite.FavoriteFragment
import com.diplomproject.view.main_fragment.MainFragment
import com.diplomproject.view.root.StartingFragment
import com.diplomproject.view.root.favorite.FavoritesElementFragment
import com.diplomproject.view.root.grade.GradeFragment
import com.diplomproject.view.settings_menu.AboutApplicationFragment
import com.diplomproject.view.settings_menu.EnterExitFragment
import com.diplomproject.view.settings_menu.LoginFragment
import com.diplomproject.view.settings_menu.PrivacyPoliticFragment
import com.diplomproject.view.settings_menu.RegistrationFragment
import com.diplomproject.view.settings_menu.SettingsFragment
import com.diplomproject.view.settings_menu.SettingsNotificationFragment
import com.diplomproject.view.settings_menu.ShareApplicationFragment
import com.diplomproject.view.settings_menu.SupportFragment
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AndroidScreens : IScreens {
    override fun startMainFragment() = FragmentScreen { MainFragment.newInstance() }

    override fun startDescriptionFragment(dataModel: DataModel): Screen = FragmentScreen {
        DescriptionFragment.newInstance(Bundle().apply {
            putParcelable(CURRENT_DATA_MODEl, dataModel) })
    }

    override fun startFavoriteFragment() = FragmentScreen { FavoriteFragment.newInstance() }
    override fun startSettingsFragment() = FragmentScreen { SettingsFragment.newInstance() }
    override fun startRegistrationFragment() = FragmentScreen { RegistrationFragment.newInstance() }
    override fun startSettingsNotificationFragment() =
        FragmentScreen { SettingsNotificationFragment.newInstance() }

    override fun startSupportFragment() = FragmentScreen { SupportFragment.newInstance() }

    override fun startShareApplicationFragment() =
        FragmentScreen { ShareApplicationFragment.newInstance() }

    override fun startEnterExitFragment() = FragmentScreen { EnterExitFragment.newInstance() }
    override fun startAboutApplicationFragment() =
        FragmentScreen { AboutApplicationFragment.newInstance() }

    override fun startStartingFragment(): Screen =
        FragmentScreen { StartingFragment.newInstance() }

    override fun startFavoritesElementFragment(): Screen =
        FragmentScreen { FavoritesElementFragment.newInstance() }

    override fun startFavouritesFragment(): Screen =
        FragmentScreen { FavouritesFragment.newInstance() }

    override fun startGradeFragment(): Screen =
        FragmentScreen { GradeFragment.newInstance() }

    override fun startLoginFragment(): Screen =
        FragmentScreen { LoginFragment.newInstance() }

    override fun startPoliticFragment(): Screen =
        FragmentScreen { PrivacyPoliticFragment.newInstance() }


}



