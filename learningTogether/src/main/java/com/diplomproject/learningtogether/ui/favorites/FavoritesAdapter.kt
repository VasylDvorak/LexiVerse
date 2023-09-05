package com.diplomproject.learningtogether.ui.favorites

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.ui.lessons.LessonViewHolder

class FavoritesAdapter(
    private var data: MutableList<FavoriteLessonEntity> = mutableListOf(),
    private var listener: (Long, FavoriteLessonEntity) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<LessonViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(favorite: MutableList<FavoriteLessonEntity>) {
        data = favorite
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Long, FavoriteLessonEntity) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_lesson_full_width, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.courseId, item)
    }

    private fun getItem(position: Int): FavoriteLessonEntity = data[position]

    override fun getItemCount(): Int = data.size
    fun addData(favoriteLessonEntity: FavoriteLessonEntity?) {
        if (favoriteLessonEntity != null) {
            data.add(favoriteLessonEntity)
            notifyDataSetChanged()
        }
    }

}