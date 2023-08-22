package com.diplomproject.learningtogether.ui.grade

import android.os.Bundle
import android.view.View
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingFragment
import com.diplomproject.learningtogether.databinding.FragmentGradeBinding
import com.diplomproject.learningtogether.domain.GradeEvaluation
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.domain.interactor.GradeEvaluator
import com.diplomproject.learningtogether.utils.getColor
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import org.koin.android.ext.android.inject
import java.util.Calendar

class GradeFragment : ViewBindingFragment<FragmentGradeBinding>(
    FragmentGradeBinding::inflate
) {
    private val answer: AnswerCounterInteractor by inject()
    private val gradeEvaluator: GradeEvaluator by inject()

    private var pieEntries = listOf<PieEntry>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        val pieDataSet = PieDataSet(pieEntries, getText(R.string.grade_chart).toString())

        val pieData = PieData(pieDataSet)

        settingChart(pieData, pieDataSet)

        binding.pieChart.data = pieData

    }

    private fun getData() {
        val date = Calendar.getInstance().timeInMillis

        val positiveCounter = answer.getRightCounter(date)
        val allCounter = answer.getAllCounter(date)

        val negativeCounter = allCounter - positiveCounter

        val positiveText = getText(R.string.positive_chart).toString()
        val negativeText = getText(R.string.negative_chart).toString()

        pieEntries = listOf<PieEntry>(

            PieEntry(positiveCounter.toFloat(), positiveText),
            PieEntry(negativeCounter.toFloat(), negativeText)
        )
    }

    private fun settingChart(pieData: PieData, pieDataSet: PieDataSet) {

        val textSize = R.dimen.default_chart_text_size

        pieData.setValueTextSize(requireContext().resources.getDimension(textSize))
        pieData.setValueTextColor(R.color.black)

//        binding.pieChart.isDrawHoleEnabled = false // Убрать отверстие
        binding.pieChart.holeRadius = 40f // Радиуса отверстия

        binding.pieChart.setDrawEntryLabels(false)//отключили надписи
        binding.pieChart.setUsePercentValues(true)//Использовать процентные соотношения %

        pieDataSet.valueFormatter =
            PercentFormatter(binding.pieChart) //на графике рисуется значек %

        //Условное название графика
        binding.pieChart.description.text = getText(R.string.language_acquisition).toString()
        binding.pieChart.description.textSize =
            requireContext().resources.getDimension(textSize)

        //Цвет фона // todo не получилось задать фон
//        binding.pieChart.setBackgroundColor(R.color.bronze)

        //Раскрашиваем значения.
        pieDataSet.colors = listOf(
            GradeEvaluation.POSITIVE.getColor(requireActivity()),
            GradeEvaluation.NEGATIVE.getColor(requireActivity()),
            GradeEvaluation.UNKNOWN.getColor(requireActivity())
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = GradeFragment()
    }
}