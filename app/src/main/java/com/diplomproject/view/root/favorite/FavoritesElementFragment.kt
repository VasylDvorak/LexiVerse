package com.diplomproject.view.root.favorite

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import com.diplomproject.databinding.FragmentFavoritesElementBinding
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.ui.TogetherActivity
import com.diplomproject.view.root.TOGETHER_ACTIVITY_REQUEST_CODE
import com.diplomproject.view.root.ViewBindingFragment

class FavoritesElementFragment : ViewBindingFragment<FragmentFavoritesElementBinding>(
    FragmentFavoritesElementBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cardViewDictionaryFavorite.setOnClickListener {
                router.navigateTo(screen.startFavoriteFragment())
            }
            cardViewLearningFavorite.setOnClickListener {
                startFavoriteFragmentInTogetherActivity()
            }
        }
    }
    private fun startFavoriteFragmentInTogetherActivity() {

        val appSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putBoolean(Key.START_FAVORITES_FRAGMENT, true)
        prefsEditor.apply()
        val intent = Intent(context, TogetherActivity::class.java)
        startActivityForResult(intent, TOGETHER_ACTIVITY_REQUEST_CODE)
    }

    companion object {
        fun newInstance() = FavoritesElementFragment()
    }
}