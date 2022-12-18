package aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemRowOfSearchableItemBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.RowOfSearchableItemsModel
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.viewholders.SearchedCategoryViewHolder

class SearchedCategoryAdapter(val context: Context, val searchPackNavigator: SearchPackNavigator) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<RowOfSearchableItemsModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemRowOfSearchableItemBinding.inflate(layoutInflater, parent, false)
        return SearchedCategoryViewHolder(binding, searchPackNavigator)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: RowOfSearchableItemsModel) {
        items.add(item)
    }

    fun getItems(): ArrayList<RowOfSearchableItemsModel> {
        return items
    }

    fun deleteItem(model: RowOfSearchableItemsModel) {
        items.remove(model)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        (holder as SearchedCategoryViewHolder).bind(item, context)
    }

    fun set(searchables: List<RowOfSearchableItemsModel>) {
        items.clear()
        searchables.forEach {
            items.add(it)
        }
    }

}
