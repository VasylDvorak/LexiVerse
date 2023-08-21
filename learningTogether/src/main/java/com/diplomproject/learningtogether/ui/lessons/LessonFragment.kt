package com.diplomproject.learningtogether.ui.lessons

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.di.ConnectKoinModules.mainScreenScope
import com.diplomproject.domain.base.BaseFragment
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.ui.courses.CoursesFragment
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.navigation.IScreens
import com.diplomproject.utils.ui.AlertDialogFragment
import com.diplomproject.view.main_fragment.MainViewModel
import com.diplomproject.view.test_english.TestEnglishFragment
import com.github.terrakok.cicerone.Router
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent

internal const val DEFAULT_COURSE_KEY = -1L

class LessonFragment : Fragment(R.layout.fragment_lesson) {
    lateinit var model: MainViewModel
    private val observer = Observer<AppState> { renderData(it) }
    private lateinit var adapter: LessonsAdapter
    private lateinit var progressBar: ProgressBar
    private val viewModel: LessonsViewModel by viewModel {
        parametersOf(courseId)
    }
    val router: Router by KoinJavaComponent.inject(Router::class.java)
    val screen = KoinJavaComponent.getKoin().get<IScreens>()

    private lateinit var lessonsRecyclerView: RecyclerView

    private var courseId: Long = DEFAULT_COURSE_KEY

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        courseId = arguments?.getLong(Key.COURSE_ID_ARGS_KEY) ?: DEFAULT_COURSE_KEY

        //observe - это наблюдатель
        // подписываемся на inProgressLiveData
        viewModel.inProgressLiveData.observe(viewLifecycleOwner) { inProgress ->
            //сюда приходит значение
            lessonsRecyclerView.isVisible = !inProgress
            progressBar.isVisible = inProgress
        }

        viewModel.coursesLiveData.observe(viewLifecycleOwner) {
            it?.let { adapter.setData(it.id, it.lessons) }// пополнение адаптера данными
        }

        viewModel.selectedLessonsLiveData.observe(viewLifecycleOwner) {
            getController().openLesson(courseId, it)
        }

        view.findViewById<TextView>(R.id.fragment_id_text_view).text = courseId.toString()
        view.findViewById<TextView>(R.id.fragment_id_text_view).visibility = View.GONE
        testWords()
    }

    private fun testWords() {
        view?.findViewById<Button>(R.id.test_word_button)?.setOnClickListener {

            val searchWord=view?.findViewById<EditText>(R.id.test_word)?.text.toString()
            val viewModel: MainViewModel by lazy { mainScreenScope.get() }
            model.apply {
            model = viewModel
            subscribe().observe(viewLifecycleOwner, observer)
            getData(searchWord, true)
        }
    }
}

    fun renderData(appState: AppState) {
        model.setQuery(appState)
        when (appState) {
            is AppState.Success -> {
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showAlertDialog(
                        getString(com.diplomproject.R.string.dialog_tittle_sorry),
                        getString(com.diplomproject.R.string.empty_server_response_on_success)
                    )
                } else {
                    router.navigateTo(screen.startTestEnglishFragment(data))
                }
            }

            is AppState.Loading -> {
//                showViewLoading()
//                binding.apply {
//                    if (appState.progress != null) {
//                        progressBarRound.visibility = View.GONE
//                    } else {
//                        progressBarRound.visibility = View.VISIBLE
//                    }
//                }
            }

            is AppState.Error -> {
                showAlertDialog(
                    getString(com.diplomproject.R.string.dialog_tittle_sorry),
                    getString(com.diplomproject.R.string.empty_server_response_on_success)
                )
            }
        }
    }


    protected fun showAlertDialog(title: String?, message: String?) {
        activity?.let {
            AlertDialogFragment.newInstance(title, message)
                .show(it.supportFragmentManager, BaseFragment.DIALOG_FRAGMENT_TAG)
        }
    }

    private fun initViews(view: View) {

        lessonsRecyclerView = view.findViewById(R.id.lessons_recycler_view)
        progressBar = view.findViewById(R.id.lessons_progress_courses_bar)

        //это два параметра которые принимаем на вход. Это слушатель и данные
        lessonsRecyclerView.layoutManager = LinearLayoutManager(context)

        //кэшируем адаптер чтобы его потом вызвать //флажек для разметки
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
        getController()  //Вариант 2. агресивный способ проверки наличия контроллера. Если нет контроллера, приложение свалтится на присоединение к фрагмента к активити
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