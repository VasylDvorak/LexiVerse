package com.diplomproject.view.root.grade

import com.diplomproject.databinding.FragmentGradeBinding
import com.diplomproject.view.root.ViewBindingFragment

class GradeFragment : ViewBindingFragment<FragmentGradeBinding>(
    FragmentGradeBinding::inflate
) {

    companion object {
        @JvmStatic
        fun newInstance() = GradeFragment()
    }
}