package aut.dipterv.word_gardner.interfaces

import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase

interface SearchPackNavigator {
    fun navigateToCategorySearch(type: SearchableModelBase.SearchableType)
    fun navigateToUserDetails(userId: Long)
    fun navigateDownloadDetails(type: SearchableModelBase.SearchableType, id: Long)
}
