package com.diplomproject.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.model.data_word_request.DataModel

class HistoryAdapter(
    private var onListItemClickListener: (DataModel) -> Unit,
    private var putInFavoriteListListener: (DataModel) -> Unit,
    private var playArticulationClickListener: (String) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var data: List<DataModel> = listOf()

    fun setData(data: List<DataModel>) {
        this.data = data
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

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {

                    findViewById<CardView>(R.id.card_view)
                        .setCardBackgroundColor(resources.getColor(R.color.colorHistoryCard))

                    findViewById<TextView>(R.id.header_textview_recycler_item).text = data.text

                    findViewById<TextView>(R.id.description_textview_recycler_item).text =
                        data.meanings?.get(0)?.translation?.translation

                    findViewById<TextView>(R.id.transcription_textview_recycler_item).text =
                        "[" + data.meanings?.get(0)?.transcription + "]"
                    findViewById<AppCompatImageButton>(R.id.set_favorite).setOnClickListener {
                        putInFavoriteList(data)
                    }
                    setOnClickListener { openInNewWindow(data) }
                    findViewById<AppCompatImageButton>(R.id.play_articulation).setOnClickListener {
                        data.meanings?.get(0)?.soundUrl?.let { sound_url ->
                            playArticulationClickListener(sound_url)
                        }
                    }
                }
            }
        }
    }

    private fun openInNewWindow(listItemData: DataModel) {
        onListItemClickListener(listItemData)
    }

    private fun putInFavoriteList(favoriteData: DataModel) {
        putInFavoriteListListener(favoriteData)
    }
}
