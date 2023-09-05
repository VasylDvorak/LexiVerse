package com.diplomproject.learningtogether.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.data.mapToFavoriteLesson
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.diplomproject.learningtogether.domain.repos.FavoriteLessonsRepo
import org.koin.android.ext.android.inject

class FavouritesFragment : Fragment(R.layout.fragment_lesson) {


    private lateinit var favoriteList: MutableList<LessonIdEntity>

    private lateinit var adapter: FavoritesAdapter

//    private val listener = { favoriteEntity: LessonIdEntity ->
//        fillView(favoriteEntity)
//    }

    private lateinit var recyclerView: RecyclerView

    private val favoriteRepo: FavoriteLessonsRepo by inject()
    private val coursesRepo: CoursesRepo by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        adapter.setData(mutableListOf<FavoriteLessonEntity>())

        favoriteRepo.getFavorites().forEach {
            getLesson(it.courseId, it.lessonId, true) {
                adapter.addData(it)
            }
        }

        adapter.setOnItemClickListener { _, _ ->
            TODO()
        }
    }

    private fun getLesson(
        courseId: Long,
        lessonId: Long,
        isFavorite: Boolean,
        onSuccess: (FavoriteLessonEntity?) -> Unit
    ) {
        coursesRepo.getLesson(courseId, lessonId) {
            onSuccess(it?.mapToFavoriteLesson(isFavorite, courseId))
        }
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.lessons_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FavoritesAdapter(mutableListOf(), { _, _ ->

        })
        recyclerView.adapter = adapter
        view.findViewById<View>(R.id.lessons_progress_courses_bar).isVisible = false
    }

//    private fun fillView(favouriteEntity: LessonIdEntity) {
//        adapter.setData(favoriteList)
//    }

    private fun getController(): Controller = activity as Controller

    interface Controller {
        fun openFavourite()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavouritesFragment().apply {
            arguments = Bundle().apply {
                putString(Key.FAVOURITE_ID_ARGS_KEY, "favouriteId")
            }
        }
    }
}