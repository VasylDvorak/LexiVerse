package com.diplomproject.learningtogether.domain.interactor

interface AnswerCounterInteractor {

    fun logErrorAnswer()
    fun logRightAnswer()
    fun getRightCounter(date: Long): Int
    fun getWrongCounter(date: Long): Int
    fun getAllCounter(date: Long): Int
}