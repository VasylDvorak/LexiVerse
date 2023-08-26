package com.diplomproject.navigation

import com.diplomproject.model.data_word_request.DataModel
import com.github.terrakok.cicerone.Screen

interface IScreens {

    fun startMainFragment(): Screen
    fun startDescriptionFragment(dataModel: DataModel): Screen
    fun startFavoriteFragment(): Screen
    fun startSettingsFragment(): Screen
    fun startRegistrationFragment(): Screen
    fun startSettingsNotificationFragment(): Screen
    fun startSupportFragment(): Screen
    fun startShareApplicationFragment(): Screen
    fun startEnterExitFragment(): Screen
    fun startAboutApplicationFragment(): Screen
    fun startStartingFragment(): Screen
    fun startFavoritesElementFragment(): Screen
    fun startFavouritesFragment(): Screen
    fun startGradeFragment(): Screen
    fun startLoginFragment(): Screen
    fun startPoliticFragment(): Screen

}
