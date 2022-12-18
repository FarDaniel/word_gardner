package aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view

import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase

class RowOfSearchableItemsModel(
    var title: String = ""
) {
    val searchables: ArrayList<SearchableModelBase> = ArrayList()
}