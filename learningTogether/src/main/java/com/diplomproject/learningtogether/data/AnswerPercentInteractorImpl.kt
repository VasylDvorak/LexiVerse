package com.diplomproject.learningtogether.data

import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.domain.interactor.AnswerPercentInteractor

class AnswerPercentInteractorImpl(
    private val answerCounterInteractor: AnswerCounterInteractor
) : AnswerPercentInteractor {

    override fun getRightPercent(date: Long): Double {
        val allCounter = answerCounterInteractor.getAllCounter(date).toDouble()
        val rightCounter = answerCounterInteractor.getRightCounter(date).toDouble()

        return if (allCounter == 0.0) {
            0.0
        } else {
            rightCounter / allCounter * 100
        }
    }

    override fun getWrongPercent(date: Long): Double {
        val allCounter = answerCounterInteractor.getAllCounter(date).toDouble()
        val wrongCounter = answerCounterInteractor.getWrongCounter(date).toDouble()

        return if (allCounter == 0.0) {
            0.0
        } else {
            wrongCounter / allCounter * 100
        }
    }
}