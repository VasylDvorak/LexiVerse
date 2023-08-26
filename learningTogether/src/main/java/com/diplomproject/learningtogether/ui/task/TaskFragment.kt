package com.diplomproject.learningtogether.ui.task

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.Key.DEFAULT_COURSE_ID_KEY
import com.diplomproject.learningtogether.Key.DEFAULT_LESSON_ID_KEY
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.ViewBindingFragment
import com.diplomproject.learningtogether.databinding.FragmentTaskV2Binding
import com.diplomproject.learningtogether.ui.task.answer.AnswerAdapter
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TaskFragment : ViewBindingFragment<FragmentTaskV2Binding>(
    FragmentTaskV2Binding::inflate
) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnswerAdapter

    private lateinit var favoriteMenuItem: MenuItem

    private val viewModel: TaskViewModel by viewModel {
        val courseId = arguments?.getLong(Key.THEME_ID_ARGS_KEY) ?: DEFAULT_COURSE_ID_KEY
        val lessonId = arguments?.getLong(Key.LESSON_ID_ARGS_KEY) ?: DEFAULT_LESSON_ID_KEY
        parametersOf(courseId, lessonId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initView(view)

        viewModel.inProgressLiveData.observe(viewLifecycleOwner) { inProgress ->

            binding.taskAnswerRecyclerView.isVisible = !inProgress
            binding.progressTaskBar.isVisible = inProgress
        }

        viewModel.tasksLiveData.observe(viewLifecycleOwner) { task ->

            binding.taskTextView.text = task.task

            Picasso.get().load(task.taskImageUrl).into(binding.taskImageView)
            binding.taskImageView.scaleType = ImageView.ScaleType.FIT_CENTER

            val textTask = getString(R.string.remains_to_answer)

            val learningList = viewModel.tasks.size + 1

            binding.recordsTextView.text = "$textTask $learningList"

            task?.let {
                adapter.setData(it.variantsAnswer)
                adapter.setOnItemClickListener {
                    viewModel.onAnswerSelect(it)
                }
            }
        }

        viewModel.selectedSuccessLiveData.observe(viewLifecycleOwner) {
            val listTasks = viewModel.tasksValue
            val positiveTasks = viewModel.currentValueIndex
            val negativeTasks = viewModel.negativeTasksIndex

            val percentCorrect = (negativeTasks.toDouble() / listTasks) * 100
            val percentIncorrect = 100 - percentCorrect

            getController().openSuccessScreen(
                listTasks,
                positiveTasks,
                negativeTasks,
                percentIncorrect
            )
        }

        viewModel.wrongAnswerLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, Key.SHOW_NOTICE_TASK_FRAGMENT_KEY, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView(view: View) {
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
        fun openSuccessScreen(
            listTasks: Int,
            positiveTasks: Int,
            negativeTasks: Int,
            percentIncorrect: Double
        )
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