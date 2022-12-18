package aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_results_recycler_view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemPackBinding
import aut.dipterv.word_gardner.databinding.ItemUserBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_results_recycler_view.viewholders.SearchedFoldersViewHolder
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_results_recycler_view.viewholders.SearchedPacksViewHolder
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_results_recycler_view.viewholders.SearchedUsersViewHolder
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelUser

class SearchedResultsAdapter(val searchPackNavigator: SearchPackNavigator) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<SearchableModelBase> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)



        return when (viewType) {
            SearchableModelBase.SearchableType.TYPE_SINGLE.value -> {
                SearchedPacksViewHolder(
                    ItemPackBinding.inflate(layoutInflater, parent, false),
                    searchPackNavigator
                )
            }
            SearchableModelBase.SearchableType.TYPE_MULTIPLE.value -> {
                SearchedFoldersViewHolder(
                    ItemPackBinding.inflate(layoutInflater, parent, false),
                    searchPackNavigator
                )
            }
            SearchableModelBase.SearchableType.TYPE_USER.value -> {
                SearchedUsersViewHolder(
                    ItemUserBinding.inflate(layoutInflater, parent, false),
                    searchPackNavigator
                )
            }

            else -> {
                SearchedPacksViewHolder(
                    ItemPackBinding.inflate(layoutInflater, parent, false),
                    searchPackNavigator
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is SearchedPacksViewHolder && item is SearchableModelSingle) {
            holder.bind(item)
        }

        if (holder is SearchedFoldersViewHolder && item is SearchableModelMultiple) {
            holder.bind(item)
        }

        if (holder is SearchedUsersViewHolder && item is SearchableModelUser) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type.value
    }

    fun setItems(items: List<SearchableModelBase>) {
        this.items.clear()
        this.items.addAll(items)
    }
}