package com.diplomproject.navigation

import com.diplomproject.model.data_word_request.DataModel
import com.github.terrakok.cicerone.Screen

interface IScreens {

    fun startMainFragment(): Screen
    fun startDescriptionFragment(dataModel: DataModel): Screen
    fun startHistoryFragment(): Screen
    fun startFavoriteFragment(): Screen
    fun startSettingsFragment(): Screen
    fun startRegistrationFragment(): Screen
    fun startSettingsNotificationFragment(): Screen
    fun startSupportFragment(): Screen
    fun startChoosingThemeFragment(): Screen
    fun startShareApplicationFragment(): Screen
    fun startEnterExitFragment(): Screen
    fun startAboutApplicationFragment(): Screen
    fun startStartingFragment(): Screen
    fun startKnowledgeCheckFragment(): Screen
    fun startFavoritesElementFragment(): Screen
    fun startGradeFragment(): Screen

}
