package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models

import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty
import aut.dipterv.word_gardner.local_data.models.Pack

class SearchableModelSingle(
    val pack: Pack
) : PacksModelBase(SearchableType.TYPE_SINGLE) {
    override var title = pack.packName
    override var onlineId: Long = pack.onlineId ?: -1
    override var difficulty: Difficulty = pack.difficulty
    override var color: MutableLiveData<Int> = MutableLiveData(pack.difficulty.colorCode)
    override var picked = MutableLiveData(false)
}