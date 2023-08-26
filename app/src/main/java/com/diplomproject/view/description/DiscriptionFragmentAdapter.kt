package com.diplomproject.view.description

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.databinding.DescriptionItemBinding
import com.diplomproject.model.data_description_request.Example

private const val delayPronunciation = 5000L

class DiscriptionFragmentAdapter(
    private var playArticulationClickListener: (String) -> Unit
) : RecyclerView.Adapter<DiscriptionFragmentAdapter.RecyclerItemViewHolder>() {

    private var data: List<Example> = listOf()
    fun setData(examples: List<Example>?) {
        this.data = examples ?: listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding =
            DescriptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(val binding: DescriptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(example: Example) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    binding.apply {
                        itemDesRw.text = example.text
                        playArticulation.setOnClickListener {
                            it?.apply {
                                isEnabled = false
                                postDelayed({ isEnabled = true }, delayPronunciation)
                            }
                            example?.soundUrl?.let { soundUrl ->
                                playArticulationClickListener(soundUrl)
                            }
                        }
                    }
                }
            }
        }
    }
}
