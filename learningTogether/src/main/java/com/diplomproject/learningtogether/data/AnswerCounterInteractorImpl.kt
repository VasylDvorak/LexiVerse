package com.diplomproject.learningtogether.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.utils.bpDataFormatter
import java.util.Calendar

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

    private fun getAllAnswerCounterKey(date: Long): String {
        return ALL_ANSWER_COUNTER + bpDataFormatter.format(date)
    }

    private fun getRightAnswerCounterKey(date: Long): String {
        return RIGHT_ANSWER_COUNTER + bpDataFormatter.format(date)
    }

    override fun logErrorAnswer() {
        val date = Calendar.getInstance().timeInMillis
        val allCounter = dataStore.getInt(getAllAnswerCounterKey(date), VALUE_DEFAULT)
        val edit: SharedPreferences.Editor = dataStore.edit()
        edit.putInt(getAllAnswerCounterKey(date), allCounter + BASIC_SCORE_ANSWER)
        edit.apply()
    }

    override fun logRightAnswer() {
        val date = Calendar.getInstance().timeInMillis
        val rightCounter = dataStore.getInt(getRightAnswerCounterKey(date), VALUE_DEFAULT)
        val allCounter = dataStore.getInt(getAllAnswerCounterKey(date), VALUE_DEFAULT)
        val edit: SharedPreferences.Editor = dataStore.edit()
        edit.putInt(getAllAnswerCounterKey(date), allCounter + BASIC_SCORE_ANSWER)
        edit.putInt(getRightAnswerCounterKey(date), rightCounter + BASIC_SCORE_ANSWER)
        edit.apply()
    }

    override fun getAllCounter(date: Long): Int {
        return dataStore.getInt(getAllAnswerCounterKey(date), VALUE_DEFAULT)
    }

    override fun getRightCounter(date: Long): Int {
        return dataStore.getInt(getRightAnswerCounterKey(date), VALUE_DEFAULT)
    }
}