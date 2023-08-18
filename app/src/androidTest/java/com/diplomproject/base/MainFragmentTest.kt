package com.diplomproject.base

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diplomproject.view.main_fragment.MainFragment
import org.junit.Before

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private lateinit var scenario: FragmentScenario<MainFragment>
    @Before
    fun setup () {
// Запускаем Fragment в корне Activity
        scenario = launchFragmentInContainer()
    }

}