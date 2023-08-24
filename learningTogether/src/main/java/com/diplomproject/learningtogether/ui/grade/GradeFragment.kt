package com.diplomproject.learningtogether.ui.grade

import android.os.Bundle
import android.view.View
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingFragment
import com.diplomproject.learningtogether.databinding.FragmentGradeBinding
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.domain.interactor.GradeEvaluator
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import org.koin.android.ext.android.inject
import java.util.Calendar

class GradeFragment : ViewBindingFragment<FragmentGradeBinding>(
    FragmentGradeBinding::inflate
) {
    private val answer: AnswerCounterInteractor by inject()
    private val gradeEvaluator: GradeEvaluator by inject()

    private var rightBarEntries = listOf<BarEntry>()
    private var wrongBarEntries = listOf<BarEntry>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        val rightDataSet = BarDataSet(rightBarEntries, getText(R.string.grade_chart).toString())

        val wrongDataSet = BarDataSet(wrongBarEntries, getText(R.string.grade_chart).toString())

        val barData = BarData(rightDataSet, wrongDataSet)

        settingChart(barData, rightDataSet, wrongDataSet)

        binding.barChart.data = barData

    }

    private fun getData() {

        val dayMillis = 24 * 60 * 1000 * 60

        val today = Calendar.getInstance().timeInMillis
        val yesterday = today - dayMillis
        val yesterday2 = yesterday - dayMillis
        val yesterday3 = yesterday2 - dayMillis
        val yesterday4 = yesterday3 - dayMillis

        rightBarEntries = listOf<BarEntry>(

            BarEntry(5f, answer.getRightCounter(today).toFloat()),
            BarEntry(4f, answer.getRightCounter(yesterday).toFloat()),
            BarEntry(3f, answer.getRightCounter(yesterday2).toFloat()),
            BarEntry(2f, answer.getRightCounter(yesterday3).toFloat()),
            BarEntry(1f, answer.getRightCounter(yesterday4).toFloat())
        )

        wrongBarEntries = listOf<BarEntry>(

            BarEntry(5f, answer.getWrongCounter(today).toFloat()),
            BarEntry(4f, answer.getWrongCounter(yesterday).toFloat()),
            BarEntry(3f, answer.getWrongCounter(yesterday2).toFloat()),
            BarEntry(2f, answer.getWrongCounter(yesterday3).toFloat()),
            BarEntry(1f, answer.getWrongCounter(yesterday4).toFloat())
        )
    }

    private fun settingChart(barData: BarData, rightDataSet: BarDataSet, wrongDataSet: BarDataSet) {

        val textSize = R.dimen.default_chart_text_size

        barData.setValueTextSize(requireContext().resources.getDimension(textSize))
        barData.setValueTextColor(R.color.black)

//        binding.pieChart.isDrawHoleEnabled = false // Убрать отверстие
//        binding.barChart.holeRadius = 40f // Радиуса отверстия
//
//        binding.barChart.setDrawEntryLabels(false)//отключили надписи
//        binding.barChart.setUsePercentValues(true)//Использовать процентные соотношения %

//        pieDataSet.valueFormatter =
//            PercentFormatter(binding.barChart) //на графике рисуется значек %

        //Условное название графика
        binding.barChart.description.text = getText(R.string.language_acquisition).toString()
        binding.barChart.description.textSize =
            requireContext().resources.getDimension(textSize)

        //Цвет фона // todo не получилось задать фон
//        binding.pieChart.setBackgroundColor(R.color.bronze)

        //Раскрашиваем значения.
//        rightDataSet.colors = listOf(
//            GradeEvaluation.POSITIVE.getColor(requireActivity()),
//            GradeEvaluation.NEGATIVE.getColor(requireActivity()),
//            GradeEvaluation.UNKNOWN.getColor(requireActivity())
//        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = GradeFragment()
    }
}