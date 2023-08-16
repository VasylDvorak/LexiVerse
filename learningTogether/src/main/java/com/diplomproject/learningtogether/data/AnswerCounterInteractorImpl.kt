package com.diplomproject.learningtogether.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor


class AnswerCounterInteractorImpl(
    context: Context
) : AnswerCounterInteractor {

    private val dataStore: SharedPreferences =
        context.getSharedPreferences("answer_counter", MODE_PRIVATE)

    override fun logErrorAnswer() {
        val allCounter = dataStore.getInt("all_answer_counter", 0)
        val edit: SharedPreferences.Editor = dataStore.edit()
        edit.putInt("all_answer_counter", allCounter + 1)
        edit.apply()
    }

    override fun logRightAnswer() {
        val rightCounter = dataStore.getInt("right_answer_counter", 0)
        val allCounter = dataStore.getInt("all_answer_counter", 0)
        val edit: SharedPreferences.Editor = dataStore.edit()
        edit.putInt("all_answer_counter", allCounter + 1)
        edit.putInt("right_answer_counter", rightCounter + 1)
        edit.apply()
    }

    override fun getAllCounter(): Int {
        return dataStore.getInt("all_answer_counter", 0)
    }

    override fun getRightCounter(): Int {
        return dataStore.getInt("right_answer_counter", 0)
    }
}