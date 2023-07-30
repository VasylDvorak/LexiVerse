package com.diplomproject.learningtogether.ui.favorites

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity

class FavoritesAdapter(
    private var data: MutableList<LessonIdEntity> = mutableListOf(),
    private var listener: (LessonIdEntity) -> Unit = {}
) : RecyclerView.Adapter<FavoriteViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(favorite: MutableList<LessonIdEntity>) {
        data = favorite
        notifyDataSetChanged()//обнавили данные
    }

    fun setOnItemClickListener(listener: (LessonIdEntity) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_lesson_full_width, parent, false), listener
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): LessonIdEntity = data[position]

    override fun getItemCount(): Int = data.size

}