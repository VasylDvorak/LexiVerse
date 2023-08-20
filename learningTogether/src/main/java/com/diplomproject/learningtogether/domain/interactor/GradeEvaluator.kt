package com.diplomproject.learningtogether.domain.interactor

import com.diplomproject.learningtogether.domain.GradeEvaluation

interface GradeEvaluator {

    fun evaluator(answer: AnswerCounterInteractor): GradeEvaluation
}