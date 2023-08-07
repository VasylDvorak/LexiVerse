package com.diplomproject.view.root

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

    private fun initButton() {
        binding.apply {
            with(router) {
                screen.apply {

                    dictionaryButton.setOnClickListener {
                        getController().openDictionary()
                    }

                    learningTogetherButton.setOnClickListener {
                        getController().openLearningTogether(true)
                    }

                    knowledgeCheckButton.setOnClickListener {
                        getController().openLearningTogether(false)
                    }
                }
            }
        }
    }

    interface Controller {
        fun openDictionary()
        fun openLearningTogether(flagLearningOrTest: Boolean)
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