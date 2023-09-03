package com.diplomproject.view.settings_menu

import android.animation.Animator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.diplomproject.navigation.IScreens
import com.diplomproject.view.AnimatorDictionary
import com.github.terrakok.cicerone.Router
import org.koin.java.KoinJavaComponent

abstract class BaseFragmentSettingsMenu<B : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean
    ) -> B
) : Fragment() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding!!
    var mMediaPlayer: MediaPlayer? = null
    val router: Router by KoinJavaComponent.inject(Router::class.java)
    val screen = KoinJavaComponent.getKoin().get<IScreens>()
    protected var isNetworkAvailable: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
//        return AnimatorDictionary().setAnimator(transit, enter)
//    }

}