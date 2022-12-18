package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models

import aut.dipterv.word_gardner.local_data.models.WordCard

class SearchableModelCard(
    val card: WordCard
) : SearchableModelBase(SearchableType.TYPE_CARD)