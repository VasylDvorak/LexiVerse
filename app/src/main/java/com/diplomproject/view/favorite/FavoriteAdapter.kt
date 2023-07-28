package com.diplomproject.view.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.model.data_word_request.DataModel

class FavoriteAdapter(
    private var onListItemClickListener: (DataModel) -> Unit,
    private var playArticulationClickListener: (String) -> Unit,
    private var callbackRemove: (Int, DataModel) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.RecyclerItemViewHolder>(), ItemTouchHelperAdapter {

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
        fun onItemSelected() {
            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.color_error
                )
            )
        }

        fun onItemRelease() {
            itemView
                .setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_favorite_card
                    )
                )
        }

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {

                    findViewById<CardView>(R.id.card_view)
                        .setCardBackgroundColor(resources.getColor(R.color.color_favorite_card))

                    findViewById<TextView>(R.id.header_textview_recycler_item).text = data.text

                    findViewById<TextView>(R.id.description_textview_recycler_item).text =
                        data.meanings?.get(0)?.translation?.translation

                    findViewById<TextView>(R.id.transcription_textview_recycler_item).text =
                        "[" + data.meanings?.get(0)?.transcription + "]"
                    findViewById<AppCompatImageButton>(R.id.set_favorite).visibility = View.GONE
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


    override fun onItemDismiss(position: Int) {
        callbackRemove(position, data.get(position))
    }

}
