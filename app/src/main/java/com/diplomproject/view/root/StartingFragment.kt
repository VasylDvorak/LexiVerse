package com.diplomproject.view.root

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.view.View
import com.diplomproject.databinding.FragmentStartingBinding

class StartingFragment : ViewBindingFragment<FragmentStartingBinding>(
    FragmentStartingBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? = null

    private fun initButton() {
        binding.apply {
            with(router) {
                screen.apply {

                    cardViewDictionary.setOnClickListener {
                        getController().openDictionary()
                    }

                    cardViewLearningTogether.setOnClickListener {
                        getController().openLearning()
                    }

                    cardViewTestingOfKnowlage.setOnClickListener {
                        getController().openTest()
                    }
                }
            }
        }
    }

    interface Controller {
        fun openDictionary()
        fun openLearning()
        fun openTest()
    }

    private fun getController(): Controller = activity as Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    companion object {
        fun newInstance() = StartingFragment()
    }
}