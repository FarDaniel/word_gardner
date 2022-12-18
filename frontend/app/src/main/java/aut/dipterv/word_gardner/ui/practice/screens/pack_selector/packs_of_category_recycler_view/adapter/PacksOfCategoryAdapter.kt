package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemPackBinding
import aut.dipterv.word_gardner.interfaces.PackSelectorInterface
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.PacksModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.viewholders.PacksMultipleViewHolder
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.viewholders.PacksSingleViewHolder

class PacksOfCategoryAdapter(private val packSelectionHelper: PackSelectorInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<PacksModelBase> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemPackBinding.inflate(layoutInflater, parent, false)

        return when (viewType) {
            SearchableModelBase.SearchableType.TYPE_SINGLE.value -> {
                PacksSingleViewHolder(binding, packSelectionHelper)
            }
            SearchableModelBase.SearchableType.TYPE_MULTIPLE.value -> {
                PacksMultipleViewHolder(binding, packSelectionHelper)
            }

            else -> {
                PacksSingleViewHolder(binding, packSelectionHelper)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is PacksMultipleViewHolder && item is SearchableModelMultiple) {
            holder.bind(item)
        }

        if (holder is PacksSingleViewHolder && item is SearchableModelSingle) {
            holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type.value
    }

    fun addItem(pack: SearchableModelSingle) {
        items.add(pack)
    }

    fun addAllItem(packs: List<PacksModelBase>) {

        packs.forEach { pack ->
            if (pack.type == SearchableModelBase.SearchableType.TYPE_SINGLE) {
                items.add(pack as SearchableModelSingle)
            } else if (pack.type == SearchableModelBase.SearchableType.TYPE_MULTIPLE) {
                items.add(pack as SearchableModelMultiple)
            }
        }

    }

    fun deleteItem(pack: SearchableModelSingle) {
        items.remove(pack)
    }

}