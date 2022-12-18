package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models

import aut.dipterv.word_gardner.local_data.models.User

data class SearchableModelUser(
    val user: User
) : SearchableModelBase(SearchableType.TYPE_USER)
