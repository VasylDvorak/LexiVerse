package com.diplomproject.view.root.knowledgecheck

import android.content.Context
import com.diplomproject.databinding.FragmentKnowledgeCheckBinding
import com.diplomproject.view.root.ViewBindingFragment

class KnowledgeCheckFragment : ViewBindingFragment<FragmentKnowledgeCheckBinding>(
    FragmentKnowledgeCheckBinding::inflate
) {

    interface Controller {
        //todo
    }

    private fun getController(): Controller = activity as Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    companion object {
        @JvmStatic
        fun newInstance() = KnowledgeCheckFragment()
    }
}