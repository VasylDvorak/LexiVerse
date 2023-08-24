package com.diplomproject.learningtogether.ui.grade

import android.os.Bundle
import android.view.View
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingFragment
import com.diplomproject.learningtogether.databinding.FragmentGradeBinding
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.domain.interactor.GradeEvaluator
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
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
        val yesterday1 = today - dayMillis
        val yesterday2 = yesterday1 - dayMillis
        val yesterday3 = yesterday2 - dayMillis
        val yesterday4 = yesterday3 - dayMillis
        val yesterday5 = yesterday4 - dayMillis
        val yesterday6 = yesterday5 - dayMillis

        rightBarEntries = listOf<BarEntry>(

            BarEntry(7f, answer.getRightCounter(today).toFloat()),
            BarEntry(6f, answer.getRightCounter(yesterday1).toFloat()),
            BarEntry(5f, answer.getRightCounter(yesterday2).toFloat()),
            BarEntry(4f, answer.getRightCounter(yesterday3).toFloat()),
            BarEntry(3f, answer.getRightCounter(yesterday4).toFloat()),
            BarEntry(2f, answer.getRightCounter(yesterday5).toFloat()),
            BarEntry(1f, answer.getRightCounter(yesterday6).toFloat())
        )

        wrongBarEntries = listOf<BarEntry>(

            BarEntry(7f, answer.getWrongCounter(today).toFloat()),
            BarEntry(6f, answer.getWrongCounter(yesterday1).toFloat()),
            BarEntry(5f, answer.getWrongCounter(yesterday2).toFloat()),
            BarEntry(4f, answer.getWrongCounter(yesterday3).toFloat()),
            BarEntry(3f, answer.getWrongCounter(yesterday4).toFloat()),
            BarEntry(2f, answer.getWrongCounter(yesterday5).toFloat()),
            BarEntry(1f, answer.getWrongCounter(yesterday6).toFloat())
        )
    }

    private fun settingChart(
        barData: BarData,
        rightDataSet: BarDataSet,
        wrongDataSet: BarDataSet
    ) {
        val textSize = 15F

        // Настройка осей
        val xAxis = binding.barChart.xAxis
        val leftAxis = binding.barChart.axisLeft
        val rightAxis = binding.barChart.axisRight

        xAxis.textSize = textSize
        leftAxis.textSize = textSize
        rightAxis.textSize = textSize

        // Настройка X-ось
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                // Настройка меток на X-оси
                val entryIndex = value.toInt()
                return when (entryIndex) {
                    0 -> getText(R.string.monday).toString()
                    1 -> getText(R.string.tuesday).toString()
                    2 -> getText(R.string.wednesday).toString()
                    3 -> getText(R.string.thursday).toString()
                    4 -> getText(R.string.friday).toString()
                    5 -> getText(R.string.saturday).toString()
                    6 -> getText(R.string.sunday).toString()
                    else -> ""
                }
            }
        }

        // Настройка Y-осей
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(true)
        leftAxis.gridLineWidth = 0.8f
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()}%"
            }
        }
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(true)
        rightAxis.gridLineWidth = 0.8f
        rightAxis.enableGridDashedLine(10f, 10f, 0f)
        rightAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()}%"
            }
        }

        // Настройка легенды
        val legend = binding.barChart.legend
        legend.isEnabled = true


        val groupCount = barData.entryCount // Количество групп в графике
        val startYear = 0 // Начальная позиция графика по оси X

        val groupSpace = 0.2f // Пространство между группами столбцов
        val barSpace = 0.05f // Пространство между столбцами в одной группе
        val barWidth = 0.4f // Ширина столбцов

        // Настройка данных
        rightDataSet.label = getText(R.string.right_attempts).toString()
        wrongDataSet.label = getText(R.string.wrong_attempts).toString()
        rightDataSet.color = ColorTemplate.COLORFUL_COLORS[0]
        wrongDataSet.color = ColorTemplate.COLORFUL_COLORS[1]

        // Расположение столбцов
        barData.barWidth = barWidth
        binding.barChart.data = barData // Установка данных
        binding.barChart.xAxis.axisMinimum = startYear.toFloat()
        binding.barChart.xAxis.axisMaximum =
            startYear + barWidth * groupCount + groupSpace * groupCount + barSpace * (groupCount - 1)
        binding.barChart.groupBars(startYear.toFloat(), groupSpace, barSpace)

        // Косые надписи на оси X
        binding.barChart.xAxis.labelRotationAngle = 45f
        binding.barChart.xAxis.setCenterAxisLabels(true)
        binding.barChart.xAxis.granularity = 1f
        binding.barChart.xAxis.isGranularityEnabled = true

        // Прокрутка графика по горизонтали
        binding.barChart.setVisibleXRangeMaximum(7f) // Отображать максимум 7 дней
        binding.barChart.moveViewToX(binding.barChart.xChartMax - 6f) // Проскроллить до последних 7 дней
    }

    companion object {
        @JvmStatic
        fun newInstance() = GradeFragment()
    }
}