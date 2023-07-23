package com.diplomproject.view.root.favorite

import android.content.Context
import com.diplomproject.databinding.FragmentFavoritesElementBinding
import com.diplomproject.view.root.ViewBindingFragment

class FavoritesElementFragment : ViewBindingFragment<FragmentFavoritesElementBinding>(
    FragmentFavoritesElementBinding::inflate
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
        fun newInstance() = FavoritesElementFragment()
    }
}