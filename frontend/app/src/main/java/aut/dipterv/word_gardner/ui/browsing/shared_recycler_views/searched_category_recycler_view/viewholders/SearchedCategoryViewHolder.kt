package aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.viewholders

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemRowOfSearchableItemBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.RowOfSearchableItemsModel
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_results_recycler_view.adapter.SearchedResultsAdapter

class SearchedCategoryViewHolder(
    private val binding: ItemRowOfSearchableItemBinding,
    private val searchPackNavigator: SearchPackNavigator
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: RowOfSearchableItemsModel

    fun bind(item: RowOfSearchableItemsModel, context: Context) {
        this.item = item
        binding.textItemCategoryName.text = item.title
        val recyclerView = binding.packsItemsCategory

        val searchedResultsAdapter = SearchedResultsAdapter(searchPackNavigator)

        val layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        recyclerView.layoutManager = layoutManager

        searchedResultsAdapter.setItems(item.searchables)

        recyclerView.adapter = searchedResultsAdapter

        binding.executePendingBindings()

        initListeners()

    }

    private fun initListeners() {
        binding.textItemCategoryName.setOnClickListener {
            if (item.searchables.isNotEmpty()) {
                searchPackNavigator.navigateToCategorySearch(
                    item.searchables.first().type
                )
            }
        }
    }
}
