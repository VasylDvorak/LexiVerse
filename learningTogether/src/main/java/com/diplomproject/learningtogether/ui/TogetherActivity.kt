package com.diplomproject.learningtogether.ui

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingActivity
import com.diplomproject.learningtogether.databinding.ActivityTogetherBinding
import com.diplomproject.learningtogether.domain.entities.CourseWithFavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.ui.courses.CoursesFragment
import com.diplomproject.learningtogether.ui.favorites.FavouritesFragment
import com.diplomproject.learningtogether.ui.lessons.LessonFragment
import com.diplomproject.learningtogether.ui.task.TaskFragment

class TogetherActivity : ViewBindingActivity<ActivityTogetherBinding>(
    ActivityTogetherBinding::inflate
),
    TaskFragment.Controller,
    SuccessFragment.Controller,
    CoursesFragment.Controller,
    LessonFragment.Controller,
    FavouritesFragment.Controller {

    private val defaultTitle: String by lazy { getString(R.string.app_name) }

    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_together)

        title = defaultTitle

        if (savedInstanceState == null)//проверяем какой запуск первый или нет (например, после поворота экрана)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_layout, CoursesFragment())
                .commit()
    }

    //для созранения состояния экрана (как вариант)
    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return super.onRetainCustomNonConfigurationInstance()
    }

    private fun openTaskFragment(courseId: Long, lessonId: Long) {
        val fragment: Fragment = TaskFragment.newInstance(courseId, lessonId)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_layout, fragment, Key.TEG_TASK_CONTAINER_KEY)
            .addToBackStack(null)
            .commit()
    }

    private fun openFavouritesFragment() {
        val fragment: Fragment = FavouritesFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_layout, fragment, Key.TEG_FAVOURITE_CONTAINER_KEY)
            .addToBackStack(null)
            .commit()
    }

    private fun openLessonFragment(courseId: Long) {
        Toast.makeText(this, "openLessonFragment", Toast.LENGTH_SHORT).show()
        val fragment: Fragment = LessonFragment.newInstance(courseId)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_layout, fragment, Key.SHOW_ALL_CONTAINER_KEY)

            //заменить лояут другим лояутов (фрагментом) во избежания бага
            // в представленим нового фрагмента на экране
//            .replace(R.id.container_layout, fragment, Key.SHOW_ALL_CONTAINER_KEY)
            .addToBackStack(null)
            .commit()
    }

    //открываем фрагмент при завершении заданий
    override fun openSuccessScreen() {
        supportFragmentManager.popBackStack()//чистит стэк, после появления данного фрагмента нельзя будет вернутся

        val successFragment: Fragment = SuccessFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_layout, successFragment, Key.TEG_SUCCESS_CONTAINER_KEY)
            .commit()
    }

    override fun finishSuccessFragment() {
        supportFragmentManager.findFragmentByTag(Key.TEG_SUCCESS_CONTAINER_KEY)?.let {
            supportFragmentManager
                .beginTransaction()
                .remove(it)
                .commit()
            title = defaultTitle
        }
    }

    override fun onBackPressed() {
        exitingApplicationDoubleClick()
    }

    //выход из приложения по двойному нажатию на кнопку
    private fun exitingApplicationDoubleClick() {
        if (System.currentTimeMillis() - backPressedTime <= 3_000) {
            super.onBackPressed()
            title = defaultTitle
            backPressedTime = 0//обнуляем время если вышли из фрагмента
        } else {
            Toast.makeText(
                this,
                "Нажмите еще раз, чтобы выйти из приложения", Toast.LENGTH_LONG
            )
                .show()
        }
        backPressedTime = System.currentTimeMillis()
        finishSuccessFragment()
    }

    override fun openLesson(courseId: Long, lessonEntity: FavoriteLessonEntity) {
        openTaskFragment(courseId, lessonEntity.id)
        title = lessonEntity.name
    }

    override fun openCourse(courseEntity: CourseWithFavoriteLessonEntity) {
        openLessonFragment(courseEntity.id)
        title = courseEntity.name
    }

    override fun openFavourite() {
        openFavouritesFragment()
        title = "Избранное"
    }
}