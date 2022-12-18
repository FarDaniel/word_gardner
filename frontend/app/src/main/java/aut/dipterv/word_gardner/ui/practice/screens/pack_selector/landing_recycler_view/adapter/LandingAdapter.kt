package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemRowOfSearchableItemBinding
import aut.dipterv.word_gardner.interfaces.PackSelectorInterface
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view.LandingModel
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view.viewholders.LandingViewHolder
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle

class LandingAdapter(val context: Context, val packSelectionHelper: PackSelectorInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<LandingModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemRowOfSearchableItemBinding.inflate(layoutInflater, parent, false)
        return LandingViewHolder(binding, packSelectionHelper)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: LandingModel) {
        items.add(item)
    }

    fun getItems(): ArrayList<LandingModel> {
        return items
    }

    fun getSingleItem(id: Long): SearchableModelSingle? {
        items.forEach { row ->
            row.packs.forEach { pack ->
                if (pack.type == SearchableModelBase.SearchableType.TYPE_SINGLE)
                    if ((pack as SearchableModelSingle).pack.packId == id)
                        return pack
            }
        }
        return null
    }

    fun deleteItem(model: LandingModel) {
        items.remove(model)
        notifyDataSetChanged()
    }

    fun addPackModel(model: SearchableModelMultiple) {
        var used = false
        for (i in 0 until items.size) {
            if (items[i].title == model.folder.category) {
                items[i].packs.add(model)
                used = true
            }
        }

        if (!used) {
            val newLandingModel = LandingModel(model.folder.category)
            newLandingModel.packs.add(model)
            items.add(newLandingModel)
        }
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is LandingViewHolder) {
            holder.bind(item, this, context)
        }
    }

}