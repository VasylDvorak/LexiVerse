package com.diplomproject.view.root

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.diplomproject.navigation.IScreens
import com.diplomproject.view.AnimatorTranslator
import com.github.terrakok.cicerone.Router
import org.koin.java.KoinJavaComponent

abstract class ViewBindingFragment<T : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean
    ) -> T
) : Fragment() {

    val router: Router by KoinJavaComponent.inject(Router::class.java)
    val screen = KoinJavaComponent.getKoin().get<IScreens>()

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return AnimatorTranslator().setAnimator(transit, enter)
    }

    private var _binding: T? = null

    protected val binding: T
        get() = _binding!!

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
}