package aut.dipterv.word_gardner.widgets.inputs.letters_input.letters_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemSingleLetterBinding
import aut.dipterv.word_gardner.widgets.inputs.letters_input.LettersInput

class LettersAdapter(val widget: LettersInput) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<Char>()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemSingleLetterBinding.inflate(layoutInflater, parent, false)
        return LettersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is LettersViewHolder) {
            holder.bind(item, widget)
        }
    }

    fun setFromWord(word: List<Char>) {
        items.clear()
        word.forEach {
            items.add(it)
        }
    }

    fun addItem(character: Char) {
        items.add(character)
    }

}