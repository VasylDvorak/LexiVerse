package com.diplomproject.di.koin_modules


import com.diplomproject.navigation.AndroidScreens
import com.diplomproject.navigation.IScreens
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router


class CiceroneModule {

    fun cicerone(): Cicerone<Router> = Cicerone.create()
    fun navigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder = cicerone.getNavigatorHolder()
    fun router(cicerone: Cicerone<Router>): Router = cicerone.router
    fun screens(): IScreens = AndroidScreens()
}