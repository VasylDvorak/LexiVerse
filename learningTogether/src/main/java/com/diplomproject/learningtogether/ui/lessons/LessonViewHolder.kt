package com.diplomproject.learningtogether.ui.lessons

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.R
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.interactor.FavoriteInteractor
import com.squareup.picasso.Picasso
import org.koin.java.KoinJavaComponent.inject

class LessonViewHolder(
    itemView: View,
    listener: (Long, FavoriteLessonEntity) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val likeInteractor: FavoriteInteractor by inject(FavoriteInteractor::class.java)

    private val titleTextView = itemView.findViewById<TextView>(R.id.title_text_view)
    private val coverImageView = itemView.findViewById<ImageView>(R.id.cover_image_view)
    private var favoriteImageView = itemView.findViewById<ImageView>(R.id.favorite_image_view)
    private lateinit var lessonEntity: FavoriteLessonEntity
    private var courseId: Long = -1

    fun bind(courseId: Long, lessonEntity: FavoriteLessonEntity) {
        this.lessonEntity = lessonEntity
        this.courseId = courseId
        titleTextView.text = lessonEntity.name
        //проверка на наличие катинки
        if (lessonEntity.imageUrl.isNotBlank()) {
            Picasso.get()
                .load(lessonEntity.imageUrl)
                .placeholder(R.drawable.uploading_images_5)
                .into(coverImageView)
//        coverImageView.scaleType = ImageView.ScaleType.FIT_XY// растягиваем картинку на весь элемент
        }
        //на старте делаем полное обновление состояния лайка. Подписываемся на какоето значение
        likeInteractor.onLikeChange(LessonIdEntity(courseId, lessonEntity.id)) { isFavorite ->
            //здесь ожидается какоето значение
            favoriteImageView.isVisible = isFavorite
        }
    }

    init {
        itemView.setOnClickListener {
            listener.invoke(courseId, lessonEntity)
        }

        //обрабатываем нажатие на сердечко (доставка в репозиторий и подписка обновлений)
        favoriteImageView.setOnClickListener {
            //Берем данные и просто в БД переворачиваем значения, а потом в likeInteractor придут значения
            likeInteractor.changeLike(LessonIdEntity(courseId, lessonEntity.id))
        }
    }
}