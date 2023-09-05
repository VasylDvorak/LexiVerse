package com.diplomproject.learningtogether.ui.lessons

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity

class LessonsAdapter(
    private val isFullWidth: Boolean = false,
    private var data: List<FavoriteLessonEntity> = mutableListOf(),
    private var courseId: Long = -1,
    private var listener: (Long, FavoriteLessonEntity) -> Unit = { _, _ -> },
) : RecyclerView.Adapter<LessonViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(courseId: Long, newData: List<FavoriteLessonEntity>) {
        data = newData
        this.courseId = courseId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder(
            LayoutInflater.from(parent.context).inflate(
                if (isFullWidth) {
                    R.layout.item_lesson_full_width
                } else {
                    R.layout.item_lesson
                },
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(courseId, getItem(position))
    }

    private fun getItem(pos: Int): FavoriteLessonEntity = data[pos]

    override fun getItemCount(): Int = data.size
}