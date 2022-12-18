package aut.dipterv.word_gardner.ui.practice.screens.edit_pack.new_word_recycler_view

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemNewWordBinding
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor

class NewWordViewHolder(private val binding: ItemNewWordBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var newWordModel: NewWordModel
    private lateinit var adapter: NewWordAdapter

    fun bind(newWordModel: NewWordModel, adapter: NewWordAdapter) {
        this.newWordModel = newWordModel
        this.adapter = adapter
        binding.model = newWordModel
        binding.holder = this

        binding.executePendingBindings()
        initListeners()
    }

    private fun initListeners() {

        binding.wordForeignText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                newWordModel.foreignWord = binding.wordForeignText.text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.wordMotherText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                newWordModel.nativeWord = binding.wordMotherText.text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    fun switchMenuSate() {
        if (newWordModel.editMenuVisible == View.GONE) {
            newWordModel.editMenuVisible = View.VISIBLE
        } else {
            newWordModel.editMenuVisible = View.GONE
        }
        binding.model = newWordModel
        binding.executePendingBindings()
    }

    fun setBackground(color: CardColor) {
        newWordModel.backGround = color
        switchMenuSate()
    }

    fun deleteItem(newWordModel: NewWordModel) {
        adapter.deleteItem(newWordModel)
        adapter.notifyDataSetChanged()
    }

}