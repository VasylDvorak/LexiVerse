package com.diplomproject.view.root

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.diplomproject.R
import com.diplomproject.databinding.ActivityRootBinding
import com.diplomproject.view.MainActivity
import com.diplomproject.view.root.favorite.FavoritesElementFragment
import com.diplomproject.view.root.grade.GradeFragment
import com.diplomproject.view.root.knowledgecheck.KnowledgeCheckFragment
import com.diplomproject.view.root.together.LearningTogetherActivity
import com.diplomproject.view.settings.AboutAppFragment
import com.diplomproject.view.settings.SettingsFragment

private const val TAG_ROOT_CONTAINER_LAYOUT_KEY = "TAG_ROOT_CONTAINER_LAYOUT_KEY"
private const val DICTIONARY_REQUEST_KOD = 100
private const val LEARNING_TOGETHER_REQUEST_KOD = 200

class RootActivity : ViewBindingActivity<ActivityRootBinding>(
    ActivityRootBinding::inflate
),
    SettingsFragment.Controller,
    StartingFragment.Controller,
    FavoritesElementFragment.Controller,
    GradeFragment.Controller,
    KnowledgeCheckFragment.Controller {

    private val settingsFragment: SettingsFragment by lazy { SettingsFragment.newInstance() }
    private val gradeFragment: GradeFragment by lazy {
        GradeFragment.newInstance()
    }
    private val favoritesElementFragment: FavoritesElementFragment by lazy {
        FavoritesElementFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBottomNaviBar()

        if (savedInstanceState == null) {
            binding.bottomNavBar.selectedItemId = R.id.root_menu_item
        } else {
            // todo другое
        }
    }

    private fun onBottomNaviBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.root_menu_item -> {
                    navigateTo(StartingFragment())
                }

                R.id.favorites_elements_item -> {
                    navigateTo(favoritesElementFragment)
                }

                R.id.chart_grade_item -> {
                    navigateTo(gradeFragment)
                }

                R.id.settings_item -> {
                    navigateTo(settingsFragment)
                }

                else -> throw IllegalStateException(getString(R.string.fragment_exception))
            }
            return@setOnItemSelectedListener true
        }
    }

    // Анимация перехода между фрагментами
    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out
        ).replace(
            binding.fragmentContainerFrameLayout.id, fragment,
            TAG_ROOT_CONTAINER_LAYOUT_KEY
        ).commit()
    }

    // Анимация перехода между фрагментами с BackStack
    private fun navigateWithBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out
        ).replace(
            binding.fragmentContainerFrameLayout.id, fragment,
            TAG_ROOT_CONTAINER_LAYOUT_KEY
        )
            .addToBackStack(null)
            .commit()
    }

    private fun onDictionary() {
        val intent = Intent(this, MainActivity::class.java)
        startActivityForResult(intent, DICTIONARY_REQUEST_KOD)
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavBar.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        binding.bottomNavBar.visibility = View.GONE
    }
    private fun onLearningTogether() {
        val intent = Intent(this, LearningTogetherActivity::class.java)
        startActivityForResult(intent, LEARNING_TOGETHER_REQUEST_KOD)
    }

    override fun openAboutApp() {
        navigateWithBackStack(AboutAppFragment.newInstance())
        binding.bottomNavBar.visibility = View.GONE
    }

    override fun openDictionary() {
        onDictionary()
    }

    override fun openLearningTogether() {
        onLearningTogether()
    }

    override fun openKnowledgeCheck() {
        navigateWithBackStack(
            KnowledgeCheckFragment.newInstance()
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        binding.bottomNavBar.visibility = View.VISIBLE
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            binding.bottomNavBar.visibility = View.VISIBLE

            // Возвращаемся на предыдущий экран
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                // Если в стеке нет предыдущего экрана, то вызываем действие по умолчанию:
                // закрытие приложения
                return super.onKeyDown(keyCode, event)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}