package com.diplomproject.view.settings_menu

import android.animation.Animator
import androidx.fragment.app.Fragment
import com.diplomproject.navigation.IScreens
import com.diplomproject.view.AnimatorTranslator
import com.github.terrakok.cicerone.Router
import org.koin.java.KoinJavaComponent

abstract class BaseFragmentSettingsMenu : Fragment() {

    val router: Router by KoinJavaComponent.inject(Router::class.java)
    val screen = KoinJavaComponent.getKoin().get<IScreens>()

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return AnimatorTranslator().setAnimator(transit, enter)
    }

}