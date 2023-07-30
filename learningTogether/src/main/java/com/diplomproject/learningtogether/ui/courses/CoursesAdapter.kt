package com.diplomproject.learningtogether.ui.courses

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.CourseWithFavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity

class CoursesAdapter(
    //адаптер принимает на вход данные
    private var data: List<CourseWithFavoriteLessonEntity> = mutableListOf(),
    private var onShowAllListener: (CourseWithFavoriteLessonEntity) -> Unit = {},
    private var onLessonClickListener: (Long, FavoriteLessonEntity) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<CourseViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<CourseWithFavoriteLessonEntity>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_course, parent, false),
            onLessonClickListener,
            onShowAllListener
        )
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(pos: Int): CourseWithFavoriteLessonEntity = data[pos]

    override fun getItemCount(): Int = data.size
}