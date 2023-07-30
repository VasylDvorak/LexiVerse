package com.diplomproject.view.root.favorite

import com.diplomproject.databinding.FragmentFavoritesElementBinding
import com.diplomproject.view.root.ViewBindingFragment

class FavoritesElementFragment : ViewBindingFragment<FragmentFavoritesElementBinding>(
    FragmentFavoritesElementBinding::inflate
) {

    companion object {
        @JvmStatic
        fun newInstance() = FavoritesElementFragment()
    }
}