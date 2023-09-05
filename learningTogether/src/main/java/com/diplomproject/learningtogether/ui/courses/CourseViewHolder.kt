package com.diplomproject.learningtogether.ui.courses

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
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
            onLessonClick.invoke(courseId, lessonEntity)
        }

    private val titleTextView = itemView.findViewById<TextView>(R.id.title_text_view)
    private val showAllTextView = itemView.findViewById<TextView>(R.id.show_all_text_view)

    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.lessons_recycler_view)
        .apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = lessonsAdapter

            val helper: SnapHelper = LinearSnapHelper()
            helper.attachToRecyclerView(this)
        }

    private lateinit var courseEntity: CourseWithFavoriteLessonEntity


    fun bind(courseEntity: CourseWithFavoriteLessonEntity) {
        this.courseEntity = courseEntity
        titleTextView.text = courseEntity.name

        lessonsAdapter.setData(courseEntity.id, courseEntity.lessons)
    }

    init {
        showAllTextView.setOnClickListener {
            onShowAllClick.invoke(courseEntity)
        }
    }
}