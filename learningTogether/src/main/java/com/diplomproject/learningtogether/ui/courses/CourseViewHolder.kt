package com.diplomproject.learningtogether.ui.courses

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.CourseWithFavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.ui.lessons.LessonsAdapter

class CourseViewHolder(
    itemView: View,
    onLessonClick: (Long, FavoriteLessonEntity) -> Unit,
    onShowAllClick: (CourseWithFavoriteLessonEntity) -> Unit
) :
    RecyclerView.ViewHolder(itemView) {

    private val lessonsAdapter: LessonsAdapter =
        LessonsAdapter { courseId, lessonEntity ->
            //здесь нам должно быть сообщено id курса
            onLessonClick.invoke(courseId, lessonEntity)
        }

    private val titleTextView = itemView.findViewById<TextView>(R.id.title_text_view)
    private val showAllTextView = itemView.findViewById<TextView>(R.id.show_all_text_view)

    private lateinit var courseEntity: CourseWithFavoriteLessonEntity

    fun bind(courseEntity: CourseWithFavoriteLessonEntity) {
        this.courseEntity = courseEntity
        titleTextView.text = courseEntity.name

        //привязываем lessonsAdapter
        lessonsAdapter.setData(courseEntity.id, courseEntity.lessons)
    }

    init {
        showAllTextView.setOnClickListener {
            onShowAllClick.invoke(courseEntity)
        }
    }
}