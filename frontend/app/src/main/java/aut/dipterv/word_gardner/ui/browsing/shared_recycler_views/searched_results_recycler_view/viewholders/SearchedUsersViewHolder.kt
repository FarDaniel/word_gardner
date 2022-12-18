package aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_results_recycler_view.viewholders

import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemUserBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelUser
import com.bumptech.glide.Glide

class SearchedUsersViewHolder(
    val binding: ItemUserBinding,
    private val searchPackNavigator: SearchPackNavigator
) : RecyclerView.ViewHolder(binding.root) {
    lateinit var item: SearchableModelUser

    fun bind(item: SearchableModelUser) {
        this.item = item
        binding.user = item.user
        if (!item.user.picture.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(item.user.picture)
                .into(binding.profilePictureImageView)
        }

        initListeners()

        binding.executePendingBindings()
    }

    private fun initListeners() {
        binding.root.setOnClickListener {
            searchPackNavigator.navigateToUserDetails(item.user.onlineId)
        }
    }
}
