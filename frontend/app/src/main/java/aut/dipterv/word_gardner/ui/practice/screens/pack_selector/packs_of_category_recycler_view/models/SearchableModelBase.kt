package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models

abstract class SearchableModelBase(
    val type: SearchableType
) {
    enum class SearchableType(val value: Int) {
        TYPE_SINGLE(0),
        TYPE_MULTIPLE(1),
        TYPE_USER(2),
        TYPE_CARD(3)
    }
}