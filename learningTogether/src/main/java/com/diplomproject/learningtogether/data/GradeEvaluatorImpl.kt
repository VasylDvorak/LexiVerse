package com.diplomproject.learningtogether.data

import com.diplomproject.learningtogether.domain.GradeEvaluation
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.domain.interactor.GradeEvaluator
import java.util.Calendar


class GradeEvaluatorImpl : GradeEvaluator {

    // todo доработать. Необходимо высчитывать процент правильных ответов, если правильных
    //  ответов больше 60 % то это положительно, иначе отрицательно.

    override fun evaluator(answer: AnswerCounterInteractor): GradeEvaluation =
        when (answer.getAllCounter(Calendar.getInstance().timeInMillis)) {
            in 6..100 -> GradeEvaluation.POSITIVE
            in 0..5 -> GradeEvaluation.NEGATIVE
            else -> GradeEvaluation.UNKNOWN
        }

}