package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.viewholders

import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemPackBinding
import aut.dipterv.word_gardner.interfaces.PackSelectorInterface
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle

class PacksMultipleViewHolder(
    private val binding: ItemPackBinding,
    private val packSelectionHelper: PackSelectorInterface
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var model: SearchableModelMultiple

    fun bind(model: SearchableModelMultiple) {
        this.model = model
        binding.packsModel = model

        initListeners()
    }

    private fun initListeners() {
        binding.packSurface.setOnLongClickListener {
            pickModels()
            return@setOnLongClickListener true
        }

        binding.packSurface.setOnClickListener {
            if (packSelectionHelper.pickingActive.value == true) {
                pickModels()
            } else {
                packSelectionHelper.navigateToTestFragment(model.packs)
            }
        }
        binding.editButton.setOnClickListener {
            packSelectionHelper.pickingActive.value = true
            packSelectionHelper.packsActive.value = true
            packSelectionHelper.deselectAllPacks()
            packSelectionHelper.selectPacks(model.packs)
            packSelectionHelper.editFolder(model)
            model.color.value = Difficulty.EASY.colorCode
        }
        model.color.observe(packSelectionHelper.lifecycleOwner) {
            binding.packsModel = model
            binding.executePendingBindings()
        }
    }

    private fun pickModels() {
        val nonPickedPacks = ArrayList<SearchableModelSingle>()
        var everyPackPicked = true
        for (i in 0 until model.packs.size) {
            if (model.packs[i].picked.value != true) {
                nonPickedPacks.add(model.packs[i])
                everyPackPicked = false
            }
        }
        if (!everyPackPicked) {
            packSelectionHelper.pickingActive.value = true
            packSelectionHelper.packsActive.value = true
            packSelectionHelper.selectPacks(model.packs)
        } else {
            packSelectionHelper.deselectPacks(model.packs)
        }
    }

}