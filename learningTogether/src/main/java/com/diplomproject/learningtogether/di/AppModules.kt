package com.diplomproject.learningtogether.di

/**
 * этот класс некое объединение зависимостей в общею "кучу, пакет"
 *
 * Зависимость - это любая сложная сущьность которую нужно создавать (репозитории ....)
 *
 * single (одиночный объект) — создается объект, который сохраняется в течение всего периода существования контейнера (аналогично синглтону)
 * factory (фабрика объектов) — каждый раз создается новый объект, без сохранения в контейнере (совместное использование невозможно)
 * scoped (объект в области) — создается объект, который сохраняется в рамках периода существования связанной временной области.
 *
 * get— разрешает компонентные зависимости. get() для получения необходимой зависимости из контейнера Koin.
 * get - ходит по всем зависимостям и ищит ту зависимость которая поможет ему вернуть значение.
 */

import com.diplomproject.learningtogether.data.AssetsCoursesRepoImpl
import com.diplomproject.learningtogether.data.CoursesWithFavoriteLessonInteractorImpl
import com.diplomproject.learningtogether.data.FavoriteInteractionImpl
import com.diplomproject.learningtogether.data.FavoriteRepoImpl
import com.diplomproject.learningtogether.data.retrofit.MeaningRetrofitImpl
import com.diplomproject.learningtogether.domain.interactor.CoursesWithFavoriteLessonInteractor
import com.diplomproject.learningtogether.domain.interactor.FavoriteInteractor
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.diplomproject.learningtogether.domain.repos.FavoriteLessonsRepo
import com.diplomproject.learningtogether.domain.repos.MeaningRepo
import com.diplomproject.learningtogether.ui.courses.CoursesViewModel
import com.diplomproject.learningtogether.ui.learning.LearningViewModel
import com.diplomproject.learningtogether.ui.lessons.LessonsViewModel
import com.diplomproject.learningtogether.ui.task.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModuleLearningTogether = module {

//    single<CoursesRepo> { FirebaseLessonsRepoImpl() }
    single<CoursesRepo> { AssetsCoursesRepoImpl(get()) }
    single<FavoriteLessonsRepo> { FavoriteRepoImpl() }
    single<CoursesWithFavoriteLessonInteractor> {
        CoursesWithFavoriteLessonInteractorImpl(
            get(),
            get()
        )
    }

    single<FavoriteLessonsRepo> { FavoriteRepoImpl() }

    single<FavoriteInteractor> { FavoriteInteractionImpl(get()) }

    single<MeaningRepo> { MeaningRetrofitImpl() }

    //секция viewModel
    viewModel { CoursesViewModel(get()) }
    viewModel { parameters -> LessonsViewModel(get(), parameters.get()) }
    viewModel { parameters ->
        TaskViewModel(
            get(),
            courseId = parameters[0],
            lessonId = parameters[1],
            get()
        )
    }
    viewModel { parameters ->
        LearningViewModel(
            get(),
            courseId = parameters[0],
            lessonId = parameters[1],
            get(),
            get()
        )
    }
}