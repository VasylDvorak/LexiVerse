package com.diplomproject.learningtogether.ui.learning

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.Key.DEFAULT_COURSE_ID_KEY
import com.diplomproject.learningtogether.Key.DEFAULT_LESSON_ID_KEY
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingFragment
import com.diplomproject.learningtogether.databinding.FragmentLearningBinding
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LearningFragment : ViewBindingFragment<FragmentLearningBinding>(
    FragmentLearningBinding::inflate
) {

    private lateinit var favoriteMenuItem: MenuItem

    private val viewModel: LearningViewModel by viewModel {
        val courseId = arguments?.getLong(Key.THEME_ID_ARGS_KEY) ?: DEFAULT_COURSE_ID_KEY
        val lessonId = arguments?.getLong(Key.LESSON_ID_ARGS_KEY) ?: DEFAULT_LESSON_ID_KEY
        parametersOf(courseId, lessonId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        viewModel.initValues()

        viewModel.inProgressLiveData.observe(viewLifecycleOwner) { inProgress ->
            binding.progressBar.isVisible = inProgress
        }

        viewModel.learningLiveData.observe(viewLifecycleOwner) { meaning ->
            binding.nameEnglishTextView.text = meaning.task
            binding.nameTranslationTextView.text = meaning.rightAnswer

            val currentValueIndex = (viewModel.currentValueIndex + 1).toString()
            val learningList = viewModel.learningList.size

            binding.recordsTextView.text = "$learningList / $currentValueIndex"

            Picasso.get().load(meaning.taskImageUrl).into(binding.learningImageView)
            binding.learningImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        }

        viewModel.needShowFinishScreen.observe(viewLifecycleOwner) { isLast ->
            if (isLast) {
                showFinishAlertDialog()
            }
        }

        viewModel.needShowBackButton.observe(viewLifecycleOwner) {
            val position = viewModel.currentValueIndex
            if (position == 0) {
                binding.backCardButton.visibility = View.INVISIBLE
            } else {
                binding.backCardButton.visibility = View.VISIBLE
            }
        }

        binding.backCardButton.setOnClickListener {
            viewModel.navigateToPreviousValue()
        }

        binding.forwardCardButton.setOnClickListener {
            viewModel.navigateToNextValue()
        }
    }

    private fun showFinishAlertDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_custom_layout, null)
        val dialogText = dialogView.findViewById<TextView>(R.id.title_dialog_text_view)
        val continueButton = dialogView.findViewById<Button>(R.id.continue_button)
        val testsButton = dialogView.findViewById<Button>(R.id.tests_button)
        val mainMenuButton = dialogView.findViewById<Button>(R.id.main_menu_button)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(dialogView)
            .create()

        dialogText.text = getText(R.string.message_finish_finish)

        continueButton.setOnClickListener {
            alertDialog.dismiss()
            requireActivity().finish()
            getController().openLearningOrTest(true)
        }

        testsButton.setOnClickListener {
            alertDialog.dismiss()
            requireActivity().finish()
            getController().openLearningOrTest(false)
        }

        mainMenuButton.setOnClickListener {
            alertDialog.dismiss()
            requireActivity().finish()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Обработчик нажатия на задний фон диалога
        dialogView.setOnClickListener {
            Toast.makeText(requireContext(), getText(R.string.selection_error), Toast.LENGTH_SHORT)
                .show()
        }

        alertDialog.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        favoriteMenuItem = menu.findItem(R.id.favourites_icon_menu_items)
        //лайки
        viewModel.isFavoriteLiveData.observe(viewLifecycleOwner) {
            favoriteMenuItem.setIcon(
                if (it) R.drawable.favourites_icon_filled
                else R.drawable.favourites_icon
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.favourites_icon_menu_items -> {
                viewModel.onLikeClick()
                viewModel.isFavoriteLiveData.value.let {
                    if (it == true) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.delete_favorites),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.add_favorites),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    interface Controller {
        fun openLearningOrTest(flagLearningOrTest: Boolean)
    }

    private fun getController(): Controller = activity as Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    companion object {
        @JvmStatic
        fun newInstance(courseId: Long, lessonId: Long) = LearningFragment().apply {
            arguments = Bundle().apply {
                putLong(Key.THEME_ID_ARGS_KEY, courseId)
                putLong(Key.LESSON_ID_ARGS_KEY, lessonId)
            }
        }
    }
}