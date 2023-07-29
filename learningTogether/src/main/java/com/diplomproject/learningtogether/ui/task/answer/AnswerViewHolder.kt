package com.diplomproject.learningtogether.ui.task.answer

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.R

class AnswerViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val answerTextView = itemView.findViewById<TextView>(R.id.answer_text_view)
    private lateinit var answer: String
    private lateinit var listener: (String) -> Unit

    fun bind(answer: String, listener: (String) -> Unit) {
        this.answer = answer
        this.listener = listener
        answerTextView.text = answer
    }

    init {
        //обработка нажатия. при нажатии на элемент происходит вызов listener и передается в него answer который положили в bind
        itemView.setOnClickListener { _ ->
            answer.let {
                listener.invoke(it)
            }
        }
    }
}