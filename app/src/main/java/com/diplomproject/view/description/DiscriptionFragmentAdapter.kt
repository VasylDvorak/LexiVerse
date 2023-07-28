package com.diplomproject.view.description

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.utils.ui.viewById
import org.koin.java.KoinJavaComponent.getKoin

class DiscriptionFragmentAdapter(
    private var playArticulationClickListener: (String) -> Unit
) : RecyclerView.Adapter<DiscriptionFragmentAdapter.RecyclerItemViewHolder>() {

    private var data: List<Example> = listOf()
    fun setData(examples: List<Example>?) {
        this.data = examples?: listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val header_textview_recycler_item by viewById<TextView>(R.id.header_textview_recycler_item)
        private val description_textview_recycler_item by viewById<TextView>(R.id.description_textview_recycler_item)
        private val transcription_textview_recycler_item by viewById<TextView>(R.id.transcription_textview_recycler_item)
        private val set_favorite by viewById<AppCompatImageButton>(R.id.set_favorite)
        private val play_articulation by viewById<AppCompatImageButton>(R.id.play_articulation)
        fun bind(example: Example) {
            if (layoutPosition != RecyclerView.NO_POSITION) {

                itemView.findViewById<CardView>(R.id.card_view)
                    .setCardBackgroundColor(getKoin().get<Context>()
                        .resources.getColor(R.color.color_favorite_card))

                header_textview_recycler_item.text = example.text
                description_textview_recycler_item.visibility = View.GONE
                transcription_textview_recycler_item.visibility = View.GONE
                set_favorite.visibility = View.GONE
                play_articulation.setOnClickListener {
                    example?.soundUrl?.let { sound_url ->
                        playArticulationClickListener(sound_url)
                    }
                }
            }
        }
    }
}
