package com.diplomproject.learningtogether.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor

private const val ANSWER_COUNTER = "answer_counter"
private const val ALL_ANSWER_COUNTER = "all_answer_counter"
private const val RIGHT_ANSWER_COUNTER = "right_answer_counter"
private const val VALUE_DEFAULT = 0
private const val BASIC_SCORE_ANSWER = 1

class AnswerCounterInteractorImpl(
    context: Context
) : AnswerCounterInteractor {

    private val dataStore: SharedPreferences =
        context.getSharedPreferences(ANSWER_COUNTER, MODE_PRIVATE)

    override fun logErrorAnswer() {
        val allCounter = dataStore.getInt(ALL_ANSWER_COUNTER, VALUE_DEFAULT)
        val edit: SharedPreferences.Editor = dataStore.edit()
        edit.putInt(ALL_ANSWER_COUNTER, allCounter + BASIC_SCORE_ANSWER)
        edit.apply()
    }

    override fun logRightAnswer() {
        val rightCounter = dataStore.getInt(RIGHT_ANSWER_COUNTER, VALUE_DEFAULT)
        val allCounter = dataStore.getInt(ALL_ANSWER_COUNTER, VALUE_DEFAULT)
        val edit: SharedPreferences.Editor = dataStore.edit()
        edit.putInt(ALL_ANSWER_COUNTER, allCounter + BASIC_SCORE_ANSWER)
        edit.putInt(RIGHT_ANSWER_COUNTER, rightCounter + BASIC_SCORE_ANSWER)
        edit.apply()
    }

    override fun getAllCounter(): Int {
        return dataStore.getInt(ALL_ANSWER_COUNTER, VALUE_DEFAULT)
    }

    override fun getRightCounter(): Int {
        return dataStore.getInt(RIGHT_ANSWER_COUNTER, VALUE_DEFAULT)
    }
}