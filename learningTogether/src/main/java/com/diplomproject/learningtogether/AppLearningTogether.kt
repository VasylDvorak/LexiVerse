package com.diplomproject.learningtogether

import android.app.Application
import com.diplomproject.learningtogether.data.FavoriteRepoImpl
import com.diplomproject.learningtogether.di.appModuleLearningTogether
import com.diplomproject.learningtogether.domain.repos.FavoriteLessonsRepo
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class AppLearningTogether : Application() {

    val lessonFavoriteRepo: FavoriteLessonsRepo by lazy {
        FavoriteRepoImpl()
    }

//    val coursesRepo: CoursesRepo by lazy {
////        AssetsCoursesRepoImpl(this)
//        FirebaseLessonsRepoImpl()
//    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AppLearningTogether)
            modules(appModuleLearningTogether)
        }
    }
}