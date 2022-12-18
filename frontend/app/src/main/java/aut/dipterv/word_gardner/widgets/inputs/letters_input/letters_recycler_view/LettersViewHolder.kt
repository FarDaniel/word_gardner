package aut.dipterv.word_gardner.widgets.inputs.letters_input.letters_recycler_view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemSingleLetterBinding
import aut.dipterv.word_gardner.widgets.inputs.letters_input.LettersInput

class LettersViewHolder(private val binding: ItemSingleLetterBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var letter: Char? = null
    lateinit var widget: LettersInput

    fun bind(letter: Char, widget: LettersInput) {
        this.widget = widget
        this.letter = letter
        binding.letter.text = this.letter.toString()
        binding.executePendingBindings()

        initListeners()
    }

    private fun initListeners() {
        binding.root.setOnClickListener {
            letter?.let {
                widget.checkLetter(it)
                binding.root.visibility = View.INVISIBLE
            }
        }
    }
}
