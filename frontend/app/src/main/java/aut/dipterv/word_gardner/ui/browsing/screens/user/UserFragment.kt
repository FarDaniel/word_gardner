package aut.dipterv.word_gardner.ui.browsing.screens.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.MainActivity
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.FragmentUserBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.RowOfSearchableItemsModel
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.adapter.SearchedCategoryAdapter
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_votes.view.*

@AndroidEntryPoint
class UserFragment : Fragment(), SearchPackNavigator {
    val viewModel: UserViewModel by viewModels()
    private lateinit var binding: FragmentUserBinding
    private lateinit var navController: NavController
    private val args: UserFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchedCategoryAdapter
    private lateinit var searchFilter: SearchFilter

    private lateinit var packs: RowOfSearchableItemsModel
    private lateinit var folders: RowOfSearchableItemsModel

    var isUpVoted = false
    var isDownVoted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchFilter = args.filter
        adapter = SearchedCategoryAdapter(view.context, this)
        recyclerView = binding.searchFragmentRecyclerView

        packs = RowOfSearchableItemsModel("")
        folders = RowOfSearchableItemsModel("")

        binding.fragment = this

        initListeners()

        navController = findNavController()

        viewModel.updateData(args.filter)

        val layoutManager = LinearLayoutManager(
            view.context, LinearLayoutManager.VERTICAL, false
        )

        recyclerView.layoutManager = layoutManager
    }

    private fun initListeners() {
        viewModel.packs.observe(viewLifecycleOwner) { newPacks ->
            packs.title = if (newPacks.size > 1) {
                "paklik"
            } else if (newPacks.size == 1) {
                "pakli"
            } else {
                ""
            }
            packs.searchables.clear()
            newPacks.forEach {
                packs.searchables.add(SearchableModelSingle(it))
            }
            adapter.set(listOf(packs, folders))
            binding.searchFragmentRecyclerView.adapter = adapter
            binding.executePendingBindings()
        }
        viewModel.folders.observe(viewLifecycleOwner) { newFolders ->
            binding.spinner.disappear()
            folders.title = if (newFolders.size > 1) {
                "mappák"
            } else if (newFolders.size == 1) {
                "mappa"
            } else {
                ""
            }

            folders.searchables.clear()
            newFolders.forEach {
                folders.searchables.add(SearchableModelMultiple(it))
            }
            adapter.set(listOf(packs, folders))
            binding.searchFragmentRecyclerView.adapter = adapter
            binding.executePendingBindings()
        }
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.spinner.disappear()
            if (!user.picture.isNullOrEmpty()) {
                Glide.with(this).load(user.picture).into(binding.userProfilePicture)
            }
            binding.name.text = user.name
            binding.userVotesCard.downvote_number.text = shortenNumberStringHun(user.downVotes)
            binding.userVotesCard.upvote_number.text = shortenNumberStringHun(user.upVotes)
            binding.executePendingBindings()
        }
        viewModel.upVoteCnt.observe(viewLifecycleOwner) {
            binding.userVotesCard.upvote_number.text = shortenNumberStringHun(it)
        }
        viewModel.downVoteCnt.observe(viewLifecycleOwner) {
            binding.userVotesCard.downvote_number.text = shortenNumberStringHun(it)
        }
        viewModel.ownVote.observe(viewLifecycleOwner) {
            context?.let { context ->
                if (it != null) {
                    if (it.isUpvote) {
                        isUpVoted = true
                        isDownVoted = false
                        binding.userVotesCard.upvote_button.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.green
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )

                        binding.userVotesCard.downvote_button.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        isUpVoted = false
                        isDownVoted = true
                        binding.userVotesCard.upvote_button.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )

                        binding.userVotesCard.downvote_button.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.red
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    }
                } else {
                    isUpVoted = false
                    isDownVoted = false
                    binding.userVotesCard.upvote_button.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                    binding.userVotesCard.downvote_button.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }

            }
        }
        viewModel.fault.observe(viewLifecycleOwner) {
            binding.spinner.popup("Hálózati hiba történt, próbáld újra később.")
        }
        binding.userVotesCard.upvote_button.setOnClickListener {
            if (isUpVoted) {
                isUpVoted = false
                isDownVoted = false
            } else {
                isUpVoted = true
                isDownVoted = false
            }

            viewModel.vote(isUpVoted, isDownVoted)
        }
        binding.userVotesCard.downvote_button.setOnClickListener {
            if (isDownVoted) {
                isUpVoted = false
                isDownVoted = false
            } else {
                isUpVoted = false
                isDownVoted = true
            }

            viewModel.vote(isUpVoted, isDownVoted)
        }
        binding.spinner.reloadNeeded.observe(viewLifecycleOwner) {
            viewModel.updateData(args.filter)
        }
        binding.spinner.navigateBack.observe(viewLifecycleOwner) {
            (activity as MainActivity).navigateOffline()
        }
    }

    override fun navigateToCategorySearch(
        type: SearchableModelBase.SearchableType
    ) {
        navController.navigate(
            UserFragmentDirections.actionUserFragmentToTypeSearchFragment(searchFilter, type)
        )
    }

    override fun navigateToUserDetails(userId: Long) {
        throw Error("Already here!")
    }

    override fun navigateDownloadDetails(type: SearchableModelBase.SearchableType, id: Long) {
        navController.navigate(
            UserFragmentDirections.actionUserFragmentToDownloadDetailsFragment(type, id)
        )
    }

    private fun shortenNumberStringHun(num: Int): String {
        return when {
            num in 1000..999999 -> {
                (num / 1000).toString() + "e"
            }
            num > 999999 -> {
                (num / 100000).toString() + "M"
            }
            else -> {
                num.toString()
            }
        }

    }

}
