package aut.dipterv.word_gardner.widgets.fragment_extensions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetExtendableSearchBinding
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter

class ExtendableSearchWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    var easySwitch = false
    var mediumSwitch = false
    var hardSwitch = false
    val filter = MutableLiveData<SearchFilter>()

    private val binding =
        WidgetExtendableSearchBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.widget = this
    }

    fun closeDetails() {
        val appearingLayout = binding.searchDetailedSearch
        binding.searchOpenDetailedSearch.visibility = View.VISIBLE
        binding.searchOpenDetailedSearch.animate()
            .translationY(-1500f)
            .setDuration(0)
            .start()
        binding.searchOpenDetailedSearch.animate()
            .translationY(0F)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .start()
        appearingLayout.animate()
            .translationY(-1500f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .withEndAction {
                binding.searchDetailedSearch.visibility = View.GONE
            }
            .start()
    }

    fun setState(filter: SearchFilter) {
        binding.searchSearchText.setText(filter.namePart?.lowercase())
        binding.searchCategoryText.setText(filter.categoryPart?.lowercase())
        binding.searchUpvotePercentage.progress = (filter.minimalUpvotePercentage * 100).toInt()
        easySwitch = filter.easyIncluded
        mediumSwitch = filter.mediumIncluded
        hardSwitch = filter.hardIncluded
        updateEasy()
        updateMedium()
        updateHard()
    }

    fun makeSearchFilter() {
        filter.value = SearchFilter(
            namePart = eraseEmpty(binding.searchSearchText.text.toString().lowercase()),
            categoryPart = eraseEmpty(binding.searchCategoryText.text.toString().lowercase()),
            minimalUpvotePercentage = (0.0 + binding.searchUpvotePercentage.progress) / 100,
            easyIncluded = easySwitch,
            mediumIncluded = mediumSwitch,
            hardIncluded = hardSwitch
        )

        closeDetails()

    }

    fun openDetails() {
        val appearingLayout = binding.searchDetailedSearch
        binding.searchDetailedSearch.visibility = View.VISIBLE
        binding.executePendingBindings()
        appearingLayout.animate()
            .translationY(-1500f)
            .setDuration(0)
            .start()
        appearingLayout.animate()
            .translationY(0F)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .start()
        binding.searchOpenDetailedSearch.animate()
            .translationY(-1500f)
            .setDuration(500)
            .withEndAction {
                binding.searchOpenDetailedSearch.visibility = View.GONE
            }
            .start()
    }

    private fun eraseEmpty(string: String): String? {
        return if (string.isEmpty())
            null
        else
            string
    }

    private fun updateEasy() {
        if (easySwitch) {
            binding.searchEasyCard.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.green_border, null)
            binding.searchEasyButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.green,
                    null
                )
            )
        } else {
            binding.searchEasyCard.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.border, null)
            binding.searchEasyButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
        }
        binding.executePendingBindings()
    }

    private fun updateMedium() {
        if (mediumSwitch) {
            binding.searchMediumCard.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.yellow_border, null)
            binding.searchMediumButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.yellow,
                    null
                )
            )
        } else {
            binding.searchMediumCard.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.border, null)
            binding.searchMediumButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
        }
        binding.executePendingBindings()
    }

    private fun updateHard() {
        if (hardSwitch) {
            binding.searchHardCard.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.red_border, null)
            binding.searchHardButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.red,
                    null
                )
            )
        } else {
            binding.searchHardCard.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.border, null)
            binding.searchHardButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
        }
        binding.executePendingBindings()
    }

    fun switchEasy() {
        easySwitch = !easySwitch
        updateEasy()
    }

    fun switchMedium() {
        mediumSwitch = !mediumSwitch
        updateMedium()
    }

    fun switchHard() {
        hardSwitch = !hardSwitch
        updateHard()
    }

}
