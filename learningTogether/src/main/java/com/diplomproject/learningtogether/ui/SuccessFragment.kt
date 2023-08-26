package com.diplomproject.learningtogether.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingFragment
import com.diplomproject.learningtogether.databinding.FragmentSuccessBinding
import com.diplomproject.learningtogether.utils.decimalFormat

class SuccessFragment : ViewBindingFragment<FragmentSuccessBinding>(
    FragmentSuccessBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intView()

        initButton()

//        pressingBackStackButton(view)
    }

    @SuppressLint("SetTextI18n")
    private fun intView() {
        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)

        val listTasks =
            arguments?.getInt(Key.TOTAL_TASKS_KEY) ?: Key.DEFAULT_TOTAL_TASKS_KEY

        val positiveTasks =
            arguments?.getInt(Key.POSITIVE_TASKS_KEY) ?: Key.DEFAULT_POSITIVE_TASKS_KEY

        val negativeTasks =
            arguments?.getInt(Key.NEGATIVE_TASKS_KEY) ?: Key.DEFAULT_NEGATIVE_TASKS_KEY

        val percentIncorrect =
            arguments?.getDouble(Key.PERCENT_INCORRECT_KEY) ?: Key.DEFAULT_PERCENT_INCORRECT_KEY

        val allTasksText = getText(R.string.total_tasks)
        val positiveTasksText = getText(R.string.positive_answers)
        val negativeTasksText = getText(R.string.negative_answers)
        val percent = getText(R.string.material_assimilated)

        binding.totalTasksTextView.text = "$allTasksText $listTasks"
        binding.positiveAnswersTextView.text = "$positiveTasksText $positiveTasks"
        binding.negativeAnswersTextView.text = "$negativeTasksText $negativeTasks"

        val formattedPercent = decimalFormat.format(percentIncorrect)
        binding.materialAssimilatedTextView.text = "$percent $formattedPercent %"

        if (percentIncorrect.toInt() >= 60) {
            binding.materialAssimilatedTextView.setTextColor(greenColor)
        } else {
            binding.materialAssimilatedTextView.setTextColor(redColor)
        }
    }

    private fun initButton() {
        binding.testsButton.setOnClickListener {
            getController().finishSuccessFragment()
        }

        binding.mainMenuButton.setOnClickListener {
            requireActivity().finish()
        }

        binding.chartButton.setOnClickListener {
            requireActivity().finish()
        }
    }

    //это перехват нажатия на BackStack
    private fun pressingBackStackButton(view: View) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        var clickedBack = true
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK && clickedBack) {
                clickedBack = false
                Toast.makeText(
                    requireContext(),
                    "Нажмите еще раз, чтобы выйти из приложения",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }
    }

    private fun getController(): Controller = activity as? Controller
        ?: throw IllegalStateException("Активити должна наследовать контроллер!!!")

    interface Controller {
        fun finishSuccessFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            listTasks: Int,
            positiveTasks: Int,
            negativeTasks: Int,
            percentIncorrect: Double
        ) =
            SuccessFragment().apply {
                arguments = Bundle().apply {
                    putInt(Key.TOTAL_TASKS_KEY, listTasks)
                    putInt(Key.POSITIVE_TASKS_KEY, positiveTasks)
                    putInt(Key.NEGATIVE_TASKS_KEY, negativeTasks)
                    putDouble(Key.PERCENT_INCORRECT_KEY, percentIncorrect)
                }
            }
    }
}
