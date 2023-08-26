package com.diplomproject.learningtogether.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.Key.LEARNING_TOGETHER_REQUEST_KOD
import com.diplomproject.learningtogether.Key.TEG_COURSES_CONTAINER_KEY
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingActivity
import com.diplomproject.learningtogether.databinding.ActivityTogetherBinding
import com.diplomproject.learningtogether.domain.entities.CourseWithFavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.ui.courses.CoursesFragment
import com.diplomproject.learningtogether.ui.favorites.FavouritesFragment
import com.diplomproject.learningtogether.ui.learning.LearningFragment
import com.diplomproject.learningtogether.ui.lessons.LessonFragment
import com.diplomproject.learningtogether.ui.task.TaskFragment

class TogetherActivity : ViewBindingActivity<ActivityTogetherBinding>(
    ActivityTogetherBinding::inflate
),
    TaskFragment.Controller,
    SuccessFragment.Controller,
    CoursesFragment.Controller,
    LessonFragment.Controller,
    FavouritesFragment.Controller,
    LearningFragment.Controller {

    private val defaultTitle: String by lazy { getString(R.string.app_name) }

    private var backPressedTime: Long = 0
    private var flagLearningOrTest: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_together)

        title = defaultTitle

        flagLearningOrTest =
            intent.getBooleanExtra(LEARNING_TOGETHER_REQUEST_KOD, false)

        if (savedInstanceState == null)
            openCourses(flagLearningOrTest)

        title = if (flagLearningOrTest) {
            getString(R.string.learning_together)
        } else {
            getString(R.string.knowledge_check)
        }
    }

    private fun openCourses(flagLearningOrTest: Boolean) {
        val fragment: Fragment = CoursesFragment.newInstance(flagLearningOrTest)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_layout, fragment, TEG_COURSES_CONTAINER_KEY)
            .commit()
    }

    private fun onLearningTogether(flagLearningOrTest: Boolean) {
        val intent =
            Intent(
                this, TogetherActivity::class.java
            ).apply {
                putExtra(
                    Key.LEARNING_TOGETHER_REQUEST_KOD,
                    flagLearningOrTest
                )
            }
        startActivityForResult(intent, Key.TOGETHER_ACTIVITY_REQUEST_CODE)
    }

    private fun openTaskFragment(courseId: Long, lessonId: Long) {
        val fragment: Fragment = TaskFragment.newInstance(courseId, lessonId)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_layout, fragment, Key.TEG_TASK_CONTAINER_KEY)
            .addToBackStack(null)
            .commit()
    }

    private fun openLearningFragment(courseId: Long, lessonId: Long) {
        val fragment: Fragment = LearningFragment.newInstance(courseId, lessonId)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_layout, fragment, Key.TEG_LEARNING_CONTAINER_KEY)
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
            .addToBackStack(null)
            .commit()
    }

    override fun openSuccessScreen(
        listTasks: Int,
        positiveTasks: Int,
        negativeTasks: Int,
        percentIncorrect: Double
    ) {
        supportFragmentManager.popBackStack()//чистит стэк

        val successFragment: Fragment =
            SuccessFragment.newInstance(listTasks, positiveTasks, negativeTasks, percentIncorrect)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_layout, successFragment, Key.TEG_SUCCESS_CONTAINER_KEY)
            .commit()
    }

    override fun openLearningOrTest(flagLearningOrTest: Boolean) {
        onLearningTogether(flagLearningOrTest)
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
        if (!flagLearningOrTest) {
            openTaskFragment(courseId, lessonEntity.id)
        } else {
            openLearningFragment(courseId, lessonEntity.id)
        }
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