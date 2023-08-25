package com.diplomproject.view.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.databinding.RecyclerviewItemBinding
import com.diplomproject.model.data_word_request.DataModel

private const val delayButton = 400L
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
                    binding.apply {
                        cardView.setCardBackgroundColor(resources.getColor(R.color.color_favorite_card))
                        headerTextviewRecyclerItem.text = data.text
                        descriptionTextviewRecyclerItem.text =
                            data.meanings?.get(0)?.translation?.translation
                        transcriptionTextviewRecyclerItem.text =
                            "[" + data.meanings?.get(0)?.transcription + "]"
                        setFavorite.visibility = View.GONE
                        setOnClickListener { openInNewWindow(data) }
                        playArticulation.setOnClickListener {
                            it?.apply {
                                isEnabled = false
                                postDelayed({ isEnabled = true }, delayButton)
                            }
                            data.meanings?.get(0)?.soundUrl?.let { soundUrl ->
                                playArticulationClickListener(soundUrl)
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


    override fun onItemDismiss(position: Int) {
        callbackRemove(position, data.get(position))
    }

}
