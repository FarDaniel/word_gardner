package aut.dipterv.word_gardner.widgets.fragment_extensions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetPackSelectorFooterBinding

class PackSelectorFooterWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    val delete = MutableLiveData<Unit>()
    val updateFolder = MutableLiveData<Unit>()
    val makeFolder = MutableLiveData<Unit>()
    val makePack = MutableLiveData<Unit>()
    val binding = WidgetPackSelectorFooterBinding.inflate(LayoutInflater.from(context), this, true)

    val isExpanded = MutableLiveData(false)
    var isPublic = true
    var isLocked = false

    init {
        binding.root.visibility = View.INVISIBLE
        binding.widget = this

    }

    fun appear() {
        if (binding.root.height != 0) {
            binding.root.animate()
                .translationY(binding.root.height.toFloat())
                .setDuration(0)
                .start()

            binding.root.visibility = View.VISIBLE

            binding.root.animate()
                .translationY(binding.newEnvelopeLayout.height.toFloat())
                .setDuration(500)
                .start()
        }
    }

    fun switchPopupState() {
        if (isExpanded.value == true) {
            collapse()
        } else {
            expand()
        }
    }

    fun expand() {
        isExpanded.value = true
        binding.root.animate()
            .translationY(0F)
            .setDuration(500)
            .start()
    }

    fun collapse() {
        isExpanded.value = false
        binding.root.animate()
            .translationY(binding.newEnvelopeLayout.height.toFloat())
            .setDuration(500)
            .start()
    }

    fun makeNewPack() {
        makePack.value = Unit
    }

    fun setFolderName(title: String) {
        binding.packText.setText(title)
        binding.executePendingBindings()

    }

    fun setCategory(category: String) {
        binding.categoryNameText.setText(category)
        binding.executePendingBindings()
    }

    fun delete() {
        collapse()
        delete.value = Unit
    }

    fun makeFolder() {
        makeFolder.value = Unit
    }

    fun updateFolder() {
        updateFolder.value = Unit
    }

    fun switchPublic() {
        if (!isLocked) {
            isPublic = !(isPublic)
            if (isPublic) {
                binding.publicSwitch.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.green_border, null)
                binding.publicSwitchButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.green,
                        null
                    )
                )
            } else {
                binding.publicSwitch.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.border, null)
                binding.publicSwitchButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
            }
            binding.executePendingBindings()
        }
    }

    fun changeMode(isEditingFolder: Boolean, arePacksActive: Boolean) {
        if (isEditingFolder) {
            if (arePacksActive) {
                binding.autoSaveCard.visibility = View.VISIBLE
                binding.selectorFlipButton.flip(false)
            } else {
                binding.selectorFlipButton.flip(true)
            }
            binding.folderEditingLayout.visibility = View.VISIBLE
            binding.autoAddCard.visibility = View.INVISIBLE
            binding.textView.visibility = View.INVISIBLE
        } else {
            binding.folderEditingLayout.visibility = View.GONE
            if (arePacksActive) {
                binding.autoAddCard.visibility = View.VISIBLE
                binding.textView.visibility = View.INVISIBLE
                binding.selectorFlipButton.flip(false)
            } else {
                binding.textView.visibility = View.VISIBLE
                binding.autoSaveCard.visibility = View.GONE
                binding.autoAddCard.visibility = View.INVISIBLE
                binding.selectorFlipButton.flip(true)
            }
        }
        binding.executePendingBindings()
    }

}
