package com.diplomproject.learningtogether.ui.task

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.interactor.FavoriteInteractor
import com.diplomproject.learningtogether.ui.task.answer.AnswerAdapter
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent

internal const val DEFAULT_COURSE_ID_KEY = -1L
internal const val DEFAULT_LESSON_ID_KEY = -1L

class TaskFragment : Fragment(R.layout.fragment_task_v2) {

    private lateinit var taskTv: TextView
    private lateinit var taskImageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var linerLayout: LinearLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnswerAdapter

    private lateinit var favoriteMenuItem: MenuItem

    private val likeInteractor: FavoriteInteractor by KoinJavaComponent.inject(FavoriteInteractor::class.java)

    private val viewModel: TaskViewModel by viewModel {
        val courseId = arguments?.getLong(Key.THEME_ID_ARGS_KEY) ?: DEFAULT_COURSE_ID_KEY
        val lessonId = arguments?.getLong(Key.LESSON_ID_ARGS_KEY) ?: DEFAULT_LESSON_ID_KEY
        parametersOf(courseId, lessonId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initView(view)
        //observe - это наблюдатель
        // подписываемся на inProgressLiveData
        viewModel.inProgressLiveData.observe(viewLifecycleOwner) { inProgress ->
            //сюда приходит значение
            recyclerView.isVisible = !inProgress
            progressBar.isVisible = inProgress
        }

        viewModel.tasksLiveData.observe(viewLifecycleOwner) { task ->
            taskTv.text = task.task

            Picasso.get().load(task.taskImageUrl).into(taskImageView)
            taskImageView.scaleType = ImageView.ScaleType.FIT_XY

            task?.let {
                adapter.setData(it.variantsAnswer)
                adapter.setOnItemClickListener {
                    viewModel.onAnswerSelect(it)
                }
            }
        }

        viewModel.selectedSuccessLiveData.observe(viewLifecycleOwner) {
            getController().openSuccessScreen()
        }

        viewModel.wrongAnswerLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, Key.SHOW_NOTICE_TASK_FRAGMENT_KEY, Toast.LENGTH_SHORT).show()
        }

        /**
         * лайки (в данном случае приложение не стабильно работает. Иногда меню не успевает первым
         * создастся, а данные уже приходят, поэтому приложение падает. Подписку делаем в методе
         * onCreateOptionsMenu)
         */
//        viewModel.isFavoriteLiveData.observe(viewLifecycleOwner) {
//            favoriteMenuItem.setIcon(
//                if (it) R.drawable.favourites_icon_filled
//                else R.drawable.favourites_icon
//            )
//        }
    }

    private fun initView(view: View) {
        taskTv = view.findViewById(R.id.task_text_view)
        taskImageView = view.findViewById(R.id.task_image_view)
        progressBar = view.findViewById(R.id.progress_task_bar)
        linerLayout = view.findViewById(R.id.task_liner_layout)

        recyclerView = view.findViewById(R.id.task_answer_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AnswerAdapter()
        recyclerView.adapter = adapter
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
        fun newInstance(courseId: Long, lessonId: Long) = TaskFragment().apply {
            arguments = Bundle().apply {
                putLong(Key.THEME_ID_ARGS_KEY, courseId)
                putLong(Key.LESSON_ID_ARGS_KEY, lessonId)
            }
        }
    }
}