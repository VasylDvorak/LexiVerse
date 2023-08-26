package com.diplomproject.view

import android.animation.Animator
import android.animation.AnimatorInflater
import androidx.fragment.app.FragmentTransaction
import com.diplomproject.R
import org.koin.java.KoinJavaComponent.getKoin

class AnimatorDictionary {

    fun setAnimator(transit: Int, enter: Boolean): Animator? {
        return when (transit) {
            FragmentTransaction.TRANSIT_FRAGMENT_FADE -> if (enter) {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.slide_in)
            } else {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.slide_out)
            }

            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> if (enter) {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.slide_in)
            } else {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.slide_out)
            }

            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> if (enter) {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.slide_in)
            } else {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.slide_out)
            }

            else -> if (enter) {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.slide_in)
            } else {
                AnimatorInflater.loadAnimator(getKoin().get(), R.animator.slide_out)
            }
        }
    }
}