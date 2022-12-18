package aut.dipterv.word_gardner.ui.practice.screens.edit_pack.new_word_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemNewWordBinding

class NewWordAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<NewWordModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemNewWordBinding.inflate(layoutInflater, parent, false)
        return NewWordViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is NewWordViewHolder) {
            holder.bind(item, this)
        }
    }

    fun getItems(): ArrayList<NewWordModel> {
        return items
    }

    fun addItem(word: NewWordModel) {
        var appears = false
        items.forEach {
            if ((it.foreignWord.equals(word.foreignWord, ignoreCase = true)
                        && it.foreignWord != "")
                || (it.nativeWord.equals(word.nativeWord, ignoreCase = true)
                        && it.nativeWord != "")
            )
                appears = true
        }
        if (!appears)
            items.add(word)
    }

    fun deleteItem(item: NewWordModel) {
        items.remove(item)
    }

}
