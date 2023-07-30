package com.diplomproject.view.root.knowledgecheck

import com.diplomproject.databinding.FragmentKnowledgeCheckBinding
import com.diplomproject.view.root.ViewBindingFragment

class KnowledgeCheckFragment : ViewBindingFragment<FragmentKnowledgeCheckBinding>(
    FragmentKnowledgeCheckBinding::inflate
) {

    companion object {
        @JvmStatic
        fun newInstance() = KnowledgeCheckFragment()
    }
}