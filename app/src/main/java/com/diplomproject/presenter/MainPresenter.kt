package com.diplomproject.presenter

import com.github.terrakok.cicerone.Router
import com.diplomproject.navigation.IScreens
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.java.KoinJavaComponent.inject


class MainPresenter {

    private val router: Router by inject(Router::class.java)
    private val screen = getKoin().get<IScreens>()
    fun mainFragmentStart() {
        router.replaceScreen(screen.startMainFragment())

    }

    fun backClicked() {

        router.replaceScreen(screen.startMainFragment())
    }

}
