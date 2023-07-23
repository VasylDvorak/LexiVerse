package com.diplomproject.view.root.together

import android.os.Bundle
import com.diplomproject.R
import com.diplomproject.databinding.ActivityLearningTogetherBinding
import com.diplomproject.presenter.BackButtonListener
import com.diplomproject.presenter.MainPresenter
import com.diplomproject.view.root.ViewBindingActivity

class LearningTogetherActivity : ViewBindingActivity<ActivityLearningTogetherBinding>(
    ActivityLearningTogetherBinding::inflate
) {

    private val presenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_together)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()) {
                return
            }
        }
        presenter.backClicked()
    }
}