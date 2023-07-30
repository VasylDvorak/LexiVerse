package com.diplomproject.learningtogether

import android.os.Bundle
import com.diplomproject.learningtogether.databinding.ActivityLearningTogetherBinding

class LearningTogetherActivity : ViewBindingActivity<ActivityLearningTogetherBinding>(
    ActivityLearningTogetherBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_together)
    }

}