package aut.dipterv.word_gardner.widgets.fragment_extensions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetEditPackFooterBinding
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty

class EditPackFooterWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    private val binding =
        WidgetEditPackFooterBinding.inflate(LayoutInflater.from(context), this, true)

    val save = MutableLiveData<Unit>()
    val delete = MutableLiveData<Unit>()
    val makeWords = MutableLiveData<Unit>()
    val isPublic = MutableLiveData(true)
    val isExpanded = MutableLiveData(false)

    var isLocked = false
    var isNativeToAdd = false
    var packDifficulty: Difficulty = Difficulty.UNDEFINED

    init {
        binding.widget = this
        binding.root.visibility = View.INVISIBLE
    }

    fun appear() {
        if (binding.root.height != 0) {
            binding.root.animate()
                .translationY(binding.root.height.toFloat())
                .setDuration(0)
                .start()

            binding.root.visibility = View.VISIBLE

            binding.root.animate()
                .translationY(binding.footerPopups.height.toFloat())
                .setDuration(500)
                .start()
        }
    }

    fun setDifficulty(difficulty: Difficulty) {
        if (packDifficulty == difficulty) {
            packDifficulty = Difficulty.UNDEFINED
        } else {
            packDifficulty = difficulty
        }
        binding.hardCard.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.border, null)
        binding.hardButton.setTextColor(
            ResourcesCompat.getColor(
                resources,
                R.color.white,
                null
            )
        )
        binding.mediumCard.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.border, null)
        binding.mediumButton.setTextColor(
            ResourcesCompat.getColor(
                resources,
                R.color.white,
                null
            )
        )
        binding.easyCard.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.border, null)
        binding.easyButton.setTextColor(
            ResourcesCompat.getColor(
                resources,
                R.color.white,
                null
            )
        )
        when (packDifficulty) {
            Difficulty.EASY -> {
                binding.easyCard.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.green_border, null)
                binding.easyButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.green,
                        null
                    )
                )
            }
            Difficulty.MEDIUM -> {
                binding.mediumCard.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.yellow_border, null)
                binding.mediumButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.yellow,
                        null
                    )
                )
            }
            Difficulty.HARD -> {
                binding.hardCard.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.red_border, null)
                binding.hardButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.red,
                        null
                    )
                )
            }
            else -> {
            }
        }
        binding.executePendingBindings()
    }

    fun savePack() {
        save.value = Unit
    }

    fun delete() {
        delete.value = Unit
    }

    fun setIsNativeToAdd(isNative: Boolean) {
        isNativeToAdd = isNative
        binding.nativeCard.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.border, null)
        binding.foreignCard.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.border, null)
        binding.nativeButton.setTextColor(
            ResourcesCompat.getColor(
                resources,
                R.color.white,
                null
            )
        )
        binding.foreignButton.setTextColor(
            ResourcesCompat.getColor(
                resources,
                R.color.white,
                null
            )
        )
        if (isNative) {
            binding.nativeCard.foreground =
                ResourcesCompat.getDrawable(
                    resources, R.drawable.green_border, null
                )
            binding.nativeButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.green,
                    null
                )
            )
        } else {
            binding.foreignCard.foreground =
                ResourcesCompat.getDrawable(
                    resources, R.drawable.green_border, null
                )
            binding.foreignButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.green,
                    null
                )
            )
        }
        binding.executePendingBindings()
    }

    fun switchPublic() {
        if (!isLocked) {
            isPublic.value = !(isPublic.value ?: true)
            if (isPublic.value == true) {
                binding.isPublic.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.green_border, null)
                binding.isPublicButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.green,
                        null
                    )
                )
            } else {
                binding.isPublic.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.border, null)
                binding.isPublicButton.setTextColor(
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

    fun makeNewPack() {
        makeWords.value = Unit
        switchPopupState()
    }

    fun switchPopupState() {
        isExpanded.value = !(isExpanded.value ?: false)
        if (isExpanded.value == true) {

            binding.root.animate()
                .translationY(0F)
                .setDuration(500)
                .start()
        } else {
            binding.root.animate()
                .translationY(binding.footerPopups.height.toFloat())
                .setDuration(500)
                .start()
        }
    }

    fun lock() {
        isLocked = true
        binding.isPublicButton.text = "Csak frissen létrehozott pakli feltölthető."
    }

}
