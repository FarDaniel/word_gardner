package aut.dipterv.word_gardner.widgets.fragment_extensions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import aut.dipterv.word_gardner.databinding.WidgetEditPackHeaderBinding
import aut.dipterv.word_gardner.ui.practice.screens.edit_pack.EditPackFragment

class EditPackHeaderWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    private var isOpen = false
    var packName: String
        get() {
            return binding.packText.text.toString()
        }
        set(value) {
            binding.packText.setText(value)
        }
    var categoryName: String
        get() {
            return binding.categoryNameText.text.toString()
        }
        set(value) {
            binding.categoryNameText.setText(value)
        }


    private val binding =
        WidgetEditPackHeaderBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.widget = this
        binding.categoryName.layoutParams
    }

    fun switchHeaderState() {
        isOpen = !isOpen
        if (isOpen) {
            binding.pullDownIcon.visibility = View.GONE
            binding.categoryName.visibility = View.VISIBLE
            binding.packName.visibility = View.VISIBLE
        } else {
            binding.categoryName.visibility = View.GONE
            binding.packName.visibility = View.GONE
        }
        binding.executePendingBindings()
    }

    fun setFragment(fragment: EditPackFragment) {
        binding.fragment = fragment
    }

    fun setCategoryAdapter(categoryAdapter: ArrayAdapter<String>) {
        binding.categoryNameText.setAdapter(categoryAdapter)
        binding.executePendingBindings()
    }

}
