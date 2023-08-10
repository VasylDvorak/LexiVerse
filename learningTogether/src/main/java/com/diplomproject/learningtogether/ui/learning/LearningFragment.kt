package com.diplomproject.learningtogether.ui.learning

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.Key.DEFAULT_COURSE_ID_KEY
import com.diplomproject.learningtogether.Key.DEFAULT_LESSON_ID_KEY
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingFragment
import com.diplomproject.learningtogether.databinding.FragmentLearningBinding
import com.diplomproject.learningtogether.domain.repos.MeaningRepo
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LearningFragment : ViewBindingFragment<FragmentLearningBinding>(
    FragmentLearningBinding::inflate
) {

    private lateinit var favoriteMenuItem: MenuItem
//    private val meaningIndex = 0

    private val viewModel: LearningViewModel by viewModel {
        val courseId = arguments?.getLong(Key.THEME_ID_ARGS_KEY) ?: DEFAULT_COURSE_ID_KEY
        val lessonId = arguments?.getLong(Key.LESSON_ID_ARGS_KEY) ?: DEFAULT_LESSON_ID_KEY
        parametersOf(courseId, lessonId)
    }

    private val meaningRepo: MeaningRepo by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        // Инициализируйте значения
        viewModel.initValues()

        viewModel.inProgressLiveData.observe(viewLifecycleOwner) { inProgress ->
            binding.progressBar.isVisible = inProgress
        }

        viewModel.learningLiveData.observe(viewLifecycleOwner) { meaning ->
            binding.nameEnglishTextView.text = meaning.task
            binding.nameTranslationTextView.text = meaning.rightAnswer

            Picasso.get().load(meaning.taskImageUrl).into(binding.learningImageView)
            binding.learningImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        }

        viewModel.needShowFinishScreen.observe(viewLifecycleOwner) { isLast ->
            // Отобразите финишный экран, если значение true
            if (isLast) {
                // Показать финишный экран
                showFinishScreen()
            }
        }

        binding.backCardButton.setOnClickListener {
            viewModel.navigateToPreviousValue()
        }

        binding.forwardCardButton.setOnClickListener {
            viewModel.navigateToNextValue()
        }
    }

    private fun showFinishScreen() {
        // todo
        Toast.makeText(requireContext(), "FINISH", Toast.LENGTH_SHORT).show()
        // Показать финишный экран, включающий сообщение о завершении материала

        // Обработать выбор пользователя по кнопкам "Перейти к тестам" или "Изучать дальше"
    }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.favourites_icon_menu_items -> {
                viewModel.onLikeClick()
                viewModel.isFavoriteLiveData.value.let {
                    if (it == true) {
                        Toast.makeText(
                            requireContext(),
                            "Удалили из избранного",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Добавили в избранное",
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
        fun openSuccessScreen()
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