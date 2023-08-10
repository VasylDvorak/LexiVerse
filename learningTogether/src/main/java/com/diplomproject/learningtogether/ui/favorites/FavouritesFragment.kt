package com.diplomproject.learningtogether.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.repos.FavoriteLessonsRepo
import org.koin.android.ext.android.inject

class FavouritesFragment : Fragment(R.layout.fragment_lesson) {


    private lateinit var favoriteList: MutableList<LessonIdEntity>//кэшируем данные

    private lateinit var adapter: FavoritesAdapter

    private val listener = { favoriteEntity: LessonIdEntity ->
        fillView(favoriteEntity)
    }

    private lateinit var recyclerView: RecyclerView

    private val favoriteRepo: FavoriteLessonsRepo by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        adapter.setData(favoriteRepo.getFavorites() as MutableList<LessonIdEntity>)

        adapter.setOnItemClickListener {
            TODO()
        }
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.lessons_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FavoritesAdapter(mutableListOf(), listener)
        recyclerView.adapter = adapter
    }

    private fun fillView(favouriteEntity: LessonIdEntity) {
        adapter.setData(favoriteList)
    }

    private fun getController(): Controller = activity as Controller

    interface Controller {
        fun openFavourite()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()//Агресивный способ проверке налмчия контроллера
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