package com.diplomproject.view.root.grade

import android.content.Context
import com.diplomproject.databinding.FragmentGradeBinding
import com.diplomproject.view.root.ViewBindingFragment

class GradeFragment : ViewBindingFragment<FragmentGradeBinding>(
    FragmentGradeBinding::inflate
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
        fun newInstance() = GradeFragment()
    }
}