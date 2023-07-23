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
        binding.dictionaryButton.setOnClickListener {
            getController().openDictionary()
        }
        binding.learningTogetherButton.setOnClickListener {
            getController().openLearningTogether()
        }
        binding.knowledgeCheckButton.setOnClickListener {
            getController().openKnowledgeCheck()
        }
    }

    interface Controller {
        fun openDictionary()
        fun openLearningTogether()
        fun openKnowledgeCheck()
    }

    private fun getController(): Controller = activity as Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }
}