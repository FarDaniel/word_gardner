package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view.viewholders

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemRowOfSearchableItemBinding
import aut.dipterv.word_gardner.interfaces.PackSelectorInterface
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view.LandingModel
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view.adapter.LandingAdapter
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.adapter.PacksOfCategoryAdapter

class LandingViewHolder(
    private val binding: ItemRowOfSearchableItemBinding,
    private val packSelectionHelper: PackSelectorInterface
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var adapter: LandingAdapter


    fun bind(item: LandingModel, adapter: LandingAdapter, context: Context) {
        this.adapter = adapter
        binding.textItemCategoryName.text = item.title
        val packsRecyclerView = binding.packsItemsCategory

        val packsOfCategoryAdapter = PacksOfCategoryAdapter(packSelectionHelper)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        packsRecyclerView.layoutManager = layoutManager

        packsOfCategoryAdapter.addAllItem(item.packs)

        packsRecyclerView.adapter = packsOfCategoryAdapter

        binding.executePendingBindings()
    }

    fun deleteItem(model: LandingModel) {
        adapter.deleteItem(model)
        adapter.notifyDataSetChanged()
    }

}