package com.diplomproject.learningtogether.ui.grade

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingFragment
import com.diplomproject.learningtogether.databinding.FragmentGradeBinding
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.domain.interactor.AnswerPercentInteractor
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GradeFragment : ViewBindingFragment<FragmentGradeBinding>(
    FragmentGradeBinding::inflate
) {
    private val answerCounter: AnswerCounterInteractor by inject()
    private val answerPercent: AnswerPercentInteractor by inject()

    private var rightBarEntries = listOf<BarEntry>()
    private var wrongBarEntries = listOf<BarEntry>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        dataChar()

        setHasOptionsMenu(true)
    }

    private fun dataChar() {
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

            BarEntry(7f, answerPercent.getRightPercent(today).toFloat()),
            BarEntry(6f, answerPercent.getRightPercent(yesterday1).toFloat()),
            BarEntry(5f, answerPercent.getRightPercent(yesterday2).toFloat()),
            BarEntry(4f, answerPercent.getRightPercent(yesterday3).toFloat()),
            BarEntry(3f, answerPercent.getRightPercent(yesterday4).toFloat()),
            BarEntry(2f, answerPercent.getRightPercent(yesterday5).toFloat()),
            BarEntry(1f, answerPercent.getRightPercent(yesterday6).toFloat())
        )

        wrongBarEntries = listOf<BarEntry>(

            BarEntry(7f, answerPercent.getWrongPercent(today).toFloat()),
            BarEntry(6f, answerPercent.getWrongPercent(yesterday1).toFloat()),
            BarEntry(5f, answerPercent.getWrongPercent(yesterday2).toFloat()),
            BarEntry(4f, answerPercent.getWrongPercent(yesterday3).toFloat()),
            BarEntry(3f, answerPercent.getWrongPercent(yesterday4).toFloat()),
            BarEntry(2f, answerPercent.getWrongPercent(yesterday5).toFloat()),
            BarEntry(1f, answerPercent.getWrongPercent(yesterday6).toFloat())
        )
    }

    private fun settingChart(
        barData: BarData,
        rightDataSet: BarDataSet,
        wrongDataSet: BarDataSet
    ) {
        val barChart = binding.barChart
        val textSize = 15F
        val textDataSize = 12F

        /** Настройка осей */
        val xAxis = binding.barChart.xAxis
        val leftAxis = binding.barChart.axisLeft
        val rightAxis = binding.barChart.axisRight

        xAxis.textSize = textSize
        leftAxis.textSize = textSize
        rightAxis.textSize = textSize
        barData.setValueTextSize(textDataSize)
        barChart.description.textSize = 12f
        barChart.description.text = getString(R.string.description_legend)
        barChart.description.isEnabled = true

        /** Настройка X-ось */
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                val currentDateTime = Calendar.getInstance()
                currentDateTime.add(Calendar.DAY_OF_YEAR, -index)
                val dateFormat = SimpleDateFormat("dd MMM", Locale("ru"))
                return dateFormat.format(currentDateTime.time)
            }
        }

        /** Настройка Y-осей */
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

        val legend = binding.barChart.legend
        legend.isEnabled = true

        val groupCount = barData.entryCount

        /** Количество групп в графике */
        val startYear = 0

        /** Начальная позиция графика по оси X */
        val groupSpace = 0.2f

        /** Пространство между группами столбцов */
        val barSpace = 0.05f

        /** Пространство между столбцами в одной группе */
        val barWidth = 0.4f
        /** Ширина столбцов */

        rightDataSet.label = getText(R.string.right_attempts).toString()
        wrongDataSet.label = getText(R.string.wrong_attempts).toString()
        rightDataSet.color = ColorTemplate.COLORFUL_COLORS[0]
        wrongDataSet.color = ColorTemplate.COLORFUL_COLORS[1]

        /** Расположение столбцов */
        barData.barWidth = barWidth

        binding.barChart.data = barData

        barData.dataSets.reverse()

        binding.barChart.xAxis.axisMinimum = startYear.toFloat()
        binding.barChart.xAxis.axisMaximum =
            startYear + barWidth * groupCount + groupSpace * groupCount + barSpace * (groupCount - 1)
        binding.barChart.groupBars(startYear.toFloat(), groupSpace, barSpace)

        /** Косые надписи на оси X */
        binding.barChart.xAxis.labelRotationAngle = 45f
        binding.barChart.xAxis.setCenterAxisLabels(true)
        binding.barChart.xAxis.granularity = 1.25f
        binding.barChart.xAxis.isGranularityEnabled = true

        binding.barChart.setPinchZoom(false)
        binding.barChart.isDoubleTapToZoomEnabled = false
        binding.barChart.setScaleEnabled(false)
        binding.barChart.setVisibleXRangeMaximum(7f)
        binding.barChart.setVisibleXRangeMinimum(7f)
        binding.barChart.isScaleXEnabled = true
        binding.barChart.isScaleYEnabled = false

        binding.barChart.moveViewToX(binding.barChart.xChartMin - 6f)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bottom_menu, menu)
        menu.findItem(R.id.delete_data_menu_item)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_data_menu_item -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.delete_data_text))
                    .setPositiveButton(getString(R.string.yes)) { dialogInterface: DialogInterface, i: Int ->
                        answerCounter.resetCounters()
                        dialogInterface.dismiss()
                        getData()
                        dataChar()
                    }
                    .setNegativeButton(getString(R.string.no)) { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance() = GradeFragment()
    }
}