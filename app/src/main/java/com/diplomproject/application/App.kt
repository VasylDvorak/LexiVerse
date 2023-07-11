package com.diplomproject.application

import android.app.Application
import com.diplomproject.di.ConnectKoinModules.apiModule
import com.diplomproject.di.ConnectKoinModules.appModule
import com.diplomproject.di.ConnectKoinModules.application
import com.diplomproject.di.ConnectKoinModules.ciceroneModule
import com.diplomproject.di.ConnectKoinModules.favoriteFragmentModule
import com.diplomproject.di.ConnectKoinModules.favoriteScreen
import com.diplomproject.di.ConnectKoinModules.historyFragmentModule
import com.diplomproject.di.ConnectKoinModules.historyScreen
import com.diplomproject.di.ConnectKoinModules.mainFragmentModule
import com.diplomproject.di.ConnectKoinModules.mainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    application, mainScreen, apiModule, appModule,
                    mainFragmentModule, ciceroneModule, historyScreen,
                    historyFragmentModule, favoriteFragmentModule, favoriteScreen
                )
            )
        }

    }

}

