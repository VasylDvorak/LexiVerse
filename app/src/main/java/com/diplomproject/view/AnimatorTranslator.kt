package com.diplomproject.view

import android.animation.Animator
import android.animation.AnimatorInflater
import androidx.fragment.app.FragmentTransaction
import com.diplomproject.R
import org.koin.java.KoinJavaComponent.getKoin

class AnimatorTranslator {

    fun setAnimator(transit: Int, enter: Boolean): Animator? {
        return when (transit) {
            FragmentTransaction.TRANSIT_FRAGMENT_FADE -> if (enter) {
                AnimatorInflater.loadAnimator(getKoin().get(), android.R.animator.fade_in)
            } else {
                AnimatorInflater.loadAnimator(getKoin().get(), android.R.animator.fade_out)
            }

            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> if (enter) {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.fragment_pop_enter)
            } else {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.fragment_pop_exit)
            }

            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> if (enter) {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.fragment_enter)
            } else {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.fragment_exit)
            }

            else -> if (enter) {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.fragment_enter)
            } else {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.fragment_exit)
            }
        }
    }
}