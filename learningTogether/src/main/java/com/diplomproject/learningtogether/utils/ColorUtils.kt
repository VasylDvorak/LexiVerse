package com.diplomproject.learningtogether.utils

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.GradeEvaluation

/**
 * Для то чтобы можно было воспользоватся цветом из ресурсов (здесь это для графика)
 * На прямую воспользоватся цветом из ресурса нельзя поэтому нужно делать вот так
 * и тогда в коде можно сделать вот так
 * textView.setTextColor(getColorFromAttr(R.attr.color))
 * pieChart.setBackgroundColor(requireContext().getColorFromAttr(R.attr.second_plan_background))
 */
@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun GradeEvaluation.getColor(context: Context) = when (this) {
    GradeEvaluation.POSITIVE -> context.getColor(R.color.green)
    GradeEvaluation.NEGATIVE -> context.getColor(R.color.red)
    GradeEvaluation.UNKNOWN -> Color.BLUE
}