package aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemWordCardBinding

class WordCardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<WordCardModel> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemWordCardBinding.inflate(layoutInflater, parent, false)
        return WordCardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is WordCardViewHolder) {
            holder.bind(item)
        }
    }

    fun addItem(word: WordCardModel) {
        items.add(word)
    }

    fun clear() {
        items.clear()
    }

}