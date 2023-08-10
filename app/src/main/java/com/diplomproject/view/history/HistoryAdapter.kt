package com.diplomproject.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.databinding.RecyclerviewItemBinding
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
        val binding =
            RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecyclerItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(val binding: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    binding.apply {
                        cardView.setCardBackgroundColor(resources.getColor(R.color.colorHistoryCard))
                        headerTextviewRecyclerItem.text = data.text

                        descriptionTextviewRecyclerItem.text =
                            data.meanings?.get(0)?.translation?.translation

                        transcriptionTextviewRecyclerItem.text =
                            "[" + data.meanings?.get(0)?.transcription + "]"

                        setFavorite.setOnClickListener { putInFavoriteList(data) }
                        setOnClickListener { openInNewWindow(data) }
                        playArticulation.setOnClickListener {
                            it?.apply {
                                isEnabled = false
                                postDelayed({ isEnabled = true }, 400)
                            }
                            data.meanings?.get(0)?.soundUrl?.let { sound_url ->
                                playArticulationClickListener(sound_url)
                            }
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
