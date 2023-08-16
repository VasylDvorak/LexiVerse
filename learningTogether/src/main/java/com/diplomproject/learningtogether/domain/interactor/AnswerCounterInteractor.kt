package com.diplomproject.learningtogether.domain.interactor

interface AnswerCounterInteractor {

    fun logErrorAnswer()
    fun logRightAnswer()
    fun getRightCounter(): Int
    fun getAllCounter(): Int
}