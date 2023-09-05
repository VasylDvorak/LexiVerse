package com.diplomproject.learningtogether.ui.lessons

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal const val DEFAULT_COURSE_KEY = -1L

class LessonFragment : Fragment(R.layout.fragment_lesson) {

    private lateinit var adapter: LessonsAdapter
    private lateinit var progressBar: ProgressBar
    private val viewModel: LessonsViewModel by viewModel {
        parametersOf(courseId)
    }

    private lateinit var lessonsRecyclerView: RecyclerView

    private var courseId: Long = DEFAULT_COURSE_KEY

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        courseId = arguments?.getLong(Key.COURSE_ID_ARGS_KEY) ?: DEFAULT_COURSE_KEY

        viewModel.inProgressLiveData.observe(viewLifecycleOwner) { inProgress ->
            lessonsRecyclerView.isVisible = !inProgress
            progressBar.isVisible = inProgress
        }

        viewModel.coursesLiveData.observe(viewLifecycleOwner) {
            it?.let { adapter.setData(it.id, it.lessons) }
        }

        viewModel.selectedLessonsLiveData.observe(viewLifecycleOwner) {
            getController().openLesson(courseId, it)
        }
    }

    private fun initViews(view: View) {

        lessonsRecyclerView = view.findViewById(R.id.lessons_recycler_view)
        progressBar = view.findViewById(R.id.lessons_progress_courses_bar)

        lessonsRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = LessonsAdapter(
            isFullWidth = true,
        ) { courseId, lesson ->
            viewModel.onLessonClick(lesson)
        }
        lessonsRecyclerView.adapter = adapter
    }

    private fun getController(): Controller = activity as Controller

    interface Controller {
        fun openLesson(courseId: Long, lessonEntity: FavoriteLessonEntity)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    companion object {
        @JvmStatic
        fun newInstance(courseId: Long) = LessonFragment().apply {
            arguments = Bundle().apply {
                putLong(Key.COURSE_ID_ARGS_KEY, courseId)
            }
        }
    }
}