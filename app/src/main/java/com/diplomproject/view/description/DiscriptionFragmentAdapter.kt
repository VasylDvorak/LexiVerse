package com.diplomproject.view.description

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.utils.ui.viewById

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
                .inflate(R.layout.description_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val item_des_rw by viewById<TextView>(R.id.item_des_rw)
        private val play_articulation by viewById<AppCompatImageButton>(R.id.play_articulation)
        fun bind(example: Example) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                item_des_rw.text = example.text
                play_articulation.setOnClickListener {
                    it?.apply {
                        isEnabled = false
                        postDelayed({ isEnabled = true }, 1000)
                    }
                    example?.soundUrl?.let { sound_url ->
                        playArticulationClickListener(sound_url)
                    }
                }
            }
        }
    }
}
