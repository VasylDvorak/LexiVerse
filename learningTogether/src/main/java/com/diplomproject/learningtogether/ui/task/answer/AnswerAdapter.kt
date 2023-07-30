package com.diplomproject.learningtogether.ui.task.answer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.learningtogether.R

class AnswerAdapter(
    private var data: List<String> = emptyList(),
    private var listener: (String) -> Unit = {}
) : RecyclerView.Adapter<AnswerViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(answers: List<String>) {
        data = answers
        notifyDataSetChanged()//для обнавления
    }

    //сохраняем слушитель и передаем его в onCreateViewHolder
    fun setOnItemClickListener(listener: (String) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_answer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    private fun getItem(pos: Int): String = data[pos]

    override fun getItemCount(): Int = data.size
}