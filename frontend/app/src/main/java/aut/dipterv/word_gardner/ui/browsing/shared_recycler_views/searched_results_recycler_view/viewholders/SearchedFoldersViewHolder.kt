package aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_results_recycler_view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemPackBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase.SearchableType.TYPE_SINGLE
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple

class SearchedFoldersViewHolder(
    val binding: ItemPackBinding,
    private val searchPackNavigator: SearchPackNavigator
) : RecyclerView.ViewHolder(binding.root) {
    lateinit var item: SearchableModelMultiple

    fun bind(item: SearchableModelMultiple) {
        this.item = item
        binding.packsModel = item
        binding.editButton.visibility = View.GONE

        initListeners()
    }

    private fun initListeners() {
        binding.packSurface.setOnLongClickListener {
            return@setOnLongClickListener true
        }

        binding.packSurface.setOnClickListener {
            searchPackNavigator.navigateDownloadDetails(TYPE_SINGLE, item.folder.onlineId ?: 0L)
        }
    }
}