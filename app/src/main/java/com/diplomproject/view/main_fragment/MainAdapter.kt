package com.diplomproject.view.main_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.databinding.RecyclerviewItemBinding
import com.diplomproject.model.data_word_request.DataModel

class MainAdapter(

    private var onListItemClickListener: (DataModel) -> Unit,
    private var putInFavoriteListListener: (DataModel, Int, Boolean) -> Unit,
    private var playArticulationClickListener: (String) -> Unit,
) : RecyclerView.Adapter<MainAdapter.RecyclerItemViewHolder>() {

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
                        headerTextviewRecyclerItem.text = data.text
                        descriptionTextviewRecyclerItem.text =
                            data.meanings?.get(0)?.translation?.translation
                        transcriptionTextviewRecyclerItem.text =
                            "[" + data.meanings?.get(0)?.transcription + "]"

                        setFavorite.apply {
                            if (data.inFavoriteList) {
                                setImageResource(R.drawable.baseline_favorite_24)
                            } else {
                                setImageResource(R.drawable.baseline_favorite_border_24)
                            }
                            setOnClickListener {
                                data.inFavoriteList = !data.inFavoriteList
                                if (data.inFavoriteList) {
                                    setImageResource(R.drawable.baseline_favorite_24)
                                } else {
                                    setImageResource(R.drawable.baseline_favorite_border_24)
                                }
                                putInFavoriteList(data, position, data.inFavoriteList)
                            }
                        }
                        playArticulation.setOnClickListener {
                            it?.apply {
                                isEnabled = false
                                postDelayed({ isEnabled = true }, 400)
                            }
                            data.meanings?.get(0)?.soundUrl?.let { soundUrl ->
                                playArticulationClickListener(soundUrl)
                            }
                        }

                        itemView.setOnClickListener { openInNewWindow(data) }
                    }
                }
            }
        }
    }

    private fun putInFavoriteList(favoriteData: DataModel, position: Int, inFavoriteList: Boolean) {
        putInFavoriteListListener(favoriteData, position, inFavoriteList)
    }


    private fun openInNewWindow(listItemData: DataModel) {
        onListItemClickListener(listItemData)
    }
}
