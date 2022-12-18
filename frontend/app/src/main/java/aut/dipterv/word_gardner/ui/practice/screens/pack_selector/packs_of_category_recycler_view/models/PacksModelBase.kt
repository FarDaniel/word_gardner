package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models

import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty

abstract class PacksModelBase(
    val searchableType: SearchableType
) : SearchableModelBase(searchableType) {
    open val difficulty: Difficulty = Difficulty.MEDIUM
    abstract var color: MutableLiveData<Int>
    abstract var title: String
    abstract var onlineId: Long
    abstract var picked: MutableLiveData<Boolean>
}