package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view

import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.PacksModelBase

class LandingModel(val title: String = "") {
    val packs: ArrayList<PacksModelBase> = ArrayList()
}