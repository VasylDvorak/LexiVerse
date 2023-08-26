package com.diplomproject.application

import android.app.Application
import com.diplomproject.di.ConnectKoinModules.apiModule
import com.diplomproject.di.ConnectKoinModules.appModule
import com.diplomproject.di.ConnectKoinModules.application
import com.diplomproject.di.ConnectKoinModules.ciceroneModule
import com.diplomproject.di.ConnectKoinModules.descriptionFragmentModule
import com.diplomproject.di.ConnectKoinModules.descriptionScreen
import com.diplomproject.di.ConnectKoinModules.favoriteFragmentModule
import com.diplomproject.di.ConnectKoinModules.favoriteScreen
import com.diplomproject.di.ConnectKoinModules.mainFragmentModule
import com.diplomproject.di.ConnectKoinModules.mainScreen
import com.diplomproject.learningtogether.di.appModuleLearningTogether
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    application,
                    mainScreen,
                    apiModule,
                    appModule,
                    mainFragmentModule,
                    ciceroneModule,
                    favoriteFragmentModule,
                    favoriteScreen,
                    descriptionFragmentModule,
                    descriptionScreen,
                    appModuleLearningTogether
                )
            )
        }
    }
}

