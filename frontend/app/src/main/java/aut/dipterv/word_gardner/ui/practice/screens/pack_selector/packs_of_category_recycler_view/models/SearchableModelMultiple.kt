package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models

import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty
import aut.dipterv.word_gardner.local_data.models.Folder

class SearchableModelMultiple(
    var folder: Folder,
    val packs: ArrayList<SearchableModelSingle> = ArrayList()
) : PacksModelBase(SearchableType.TYPE_MULTIPLE) {
    override var difficulty = folder.difficulty
    override var title = folder.folderName
    override var onlineId: Long = folder.onlineId ?: -1
    override var color: MutableLiveData<Int> = MutableLiveData(difficulty.colorCode)
    override var picked = MutableLiveData(false)

    fun setPacks(packs: List<SearchableModelSingle>) {
        this.packs.addAll(packs)
        var sum = 0.0
        var size = 0.0

        packs.forEach { pack ->
            when (pack.difficulty) {
                Difficulty.EASY -> {
                    size++
                    sum += 1
                }
                Difficulty.MEDIUM -> {
                    size++
                    sum += 2
                }
                Difficulty.HARD -> {
                    size++
                    sum += 3
                }
                else -> {
                }
            }
            var average = -1.0
            if (size > 0)
                average = sum / size
            difficulty = if (average < 1) {
                Difficulty.UNDEFINED
            } else if (average >= 1 && average < 1.6) {
                Difficulty.EASY
            } else if (average >= 1.6 && average < 2.2) {
                Difficulty.MEDIUM
            } else {
                Difficulty.HARD
            }
            color.value = difficulty.colorCode
        }

    }
}
