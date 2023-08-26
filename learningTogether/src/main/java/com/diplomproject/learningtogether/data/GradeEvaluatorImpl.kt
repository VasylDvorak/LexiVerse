package com.diplomproject.learningtogether.data

import com.diplomproject.learningtogether.domain.GradeEvaluation
import com.diplomproject.learningtogether.domain.interactor.GradeEvaluator


class GradeEvaluatorImpl : GradeEvaluator {

    override fun evaluator(
        listTasks: Int,
        positiveTasks: Int
    ): GradeEvaluation {

        if (listTasks == 0)
            return GradeEvaluation.UNKNOWN

        val percentCorrect = (positiveTasks.toDouble() / listTasks) * 100
        return if (percentCorrect >= 60) {
            GradeEvaluation.POSITIVE
        } else {
            GradeEvaluation.NEGATIVE
        }
    }
}