package aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.viewholders

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemPackBinding
import aut.dipterv.word_gardner.interfaces.PackSelectorInterface
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle

class PacksSingleViewHolder(
    private val binding: ItemPackBinding,
    private val packSelectionHelper: PackSelectorInterface
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var model: SearchableModelSingle

    fun bind(model: SearchableModelSingle) {
        this.model = model
        binding.packsModel = model

        initListeners()
    }

    private fun initListeners() {
        binding.packSurface.setOnLongClickListener {
            pickModel()
            return@setOnLongClickListener true
        }

        binding.packSurface.setOnClickListener {
            if (packSelectionHelper.pickingActive.value == true) {
                pickModel()
            } else {
                packSelectionHelper.navigateToTestFragment(listOf(model))
            }
        }
        binding.editButton.setOnClickListener {
            packSelectionHelper.navigateToEditPackFragment(model)
        }
        model.picked.observe(packSelectionHelper.lifecycleOwner, Observer {
            binding.packsModel = model
            binding.executePendingBindings()
        })
        model.color.observe(packSelectionHelper.lifecycleOwner, Observer {
            binding.packsModel = model
            binding.executePendingBindings()
        })
    }

    private fun pickModel() {
        model.picked.value = model.picked.value != true
        if (model.picked.value == true) {
            packSelectionHelper.pickingActive.value = true
            packSelectionHelper.packsActive.value = true
            packSelectionHelper.selectPacks(listOf(model))
        } else {
            packSelectionHelper.deselectPacks(listOf(model))
        }
        binding.packsModel = model
        binding.executePendingBindings()
    }
}