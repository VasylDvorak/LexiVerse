package com.diplomproject.learningtogether.domain.interactor

interface AnswerPercentInteractor {

    fun getRightPercent(date: Long): Double
    fun getWrongPercent(date: Long): Double
}