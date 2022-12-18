package aut.dipterv.word_gardner.interfaces

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle

interface PackSelectorInterface {
    val activePacks: ArrayList<SearchableModelSingle>
    val pickingActive: MutableLiveData<Boolean>
    val packsActive: MutableLiveData<Boolean>
    val editingFolder: MutableLiveData<Boolean>
    val lifecycleOwner: LifecycleOwner

    fun navigateToTestFragment(packs: List<SearchableModelSingle>)
    fun navigateToEditPackFragment(pack: SearchableModelSingle)
    fun lastPackDeactivated()
    fun editFolder(folder: SearchableModelMultiple)
    fun switchColorStateOfPacks(inEditMode: Boolean)
    fun deselectAllPacks()
    fun selectPacks(packs: List<SearchableModelSingle>)
    fun deselectPacks(packs: List<SearchableModelSingle>)
}
