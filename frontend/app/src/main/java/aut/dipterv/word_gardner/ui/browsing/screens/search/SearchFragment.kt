package aut.dipterv.word_gardner.ui.browsing.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.MainActivity
import aut.dipterv.word_gardner.databinding.FragmentSearchBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.RowOfSearchableItemsModel
import aut.dipterv.word_gardner.ui.browsing.shared_recycler_views.searched_category_recycler_view.adapter.SearchedCategoryAdapter
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelUser
import aut.dipterv.word_gardner.widgets.shared_widgets.OnlineSpinnerWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchPackNavigator {

    val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchedCategoryAdapter

    private lateinit var packs: RowOfSearchableItemsModel
    private lateinit var folders: RowOfSearchableItemsModel
    private lateinit var users: RowOfSearchableItemsModel

    private lateinit var spinner: OnlineSpinnerWidget

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this

        adapter = SearchedCategoryAdapter(view.context, this)
        recyclerView = binding.searchFragmentRecyclerView

        packs = RowOfSearchableItemsModel("paklik")
        folders = RowOfSearchableItemsModel("mappák")
        users = RowOfSearchableItemsModel("felhasználók")

        spinner = binding.searchSpinner

        initListeners()

        navController = findNavController()

        val layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        recyclerView.layoutManager = layoutManager

    }

    override fun onResume() {
        super.onResume()

        spinner.reset()
        packs.searchables.clear()
        folders.searchables.clear()
        users.searchables.clear()
        viewModel.updateData()
        binding.extendableSearchWidget.setState(viewModel.filter)
    }

    override fun navigateToCategorySearch(
        type: SearchableModelBase.SearchableType
    ) {
        navController.navigate(
            SearchFragmentDirections.actionSearchFragmentToTypeSearchFragment(
                viewModel.filter,
                type
            )
        )
    }

    override fun navigateToUserDetails(userId: Long) {
        navController.navigate(
            SearchFragmentDirections.actionSearchFragmentToUserFragment(
                SearchFilter(
                    userId = userId
                )
            )
        )
    }

    override fun navigateDownloadDetails(type: SearchableModelBase.SearchableType, id: Long) {
        navController.navigate(
            SearchFragmentDirections.actionSearchFragmentToDownloadDetailsFragment(type, id)
        )
    }

    private fun initListeners() {
        viewModel.packs.observe(viewLifecycleOwner) { newPacks ->
            spinner.disappear()
            if (newPacks.isNotEmpty()) {
                packs.searchables.clear()
                newPacks.forEach {
                    packs.searchables.add(SearchableModelSingle(it))
                }
                adapter.set(listOf(packs, folders, users))
                binding.searchFragmentRecyclerView.adapter = adapter
                binding.executePendingBindings()
            }
        }
        viewModel.folders.observe(viewLifecycleOwner) { newFolders ->
            spinner.disappear()
            if (newFolders.isNotEmpty()) {
                folders.searchables.clear()
                newFolders.forEach {
                    folders.searchables.add(SearchableModelMultiple(it))
                }
                adapter.set(listOf(packs, folders, users))
                binding.searchFragmentRecyclerView.adapter = adapter
                binding.executePendingBindings()
            }
        }
        viewModel.users.observe(viewLifecycleOwner) { newUsers ->
            spinner.disappear()
            if (newUsers.isNotEmpty()) {
                users.searchables.clear()
                newUsers.forEach {
                    users.searchables.add(SearchableModelUser(it))
                }
                adapter.set(listOf(packs, folders, users))
                binding.searchFragmentRecyclerView.adapter = adapter
                binding.executePendingBindings()
            }
        }
        viewModel.fault.observe(viewLifecycleOwner) {
            spinner.popup("Hálózati hiba történt, próbáld újra később.")
        }
        viewModel.login.onEach {
            (activity as MainActivity).popupLogin()
        }.launchIn(lifecycleScope)
        binding.extendableSearchWidget.filter.observe(viewLifecycleOwner) { nullableNewFilter ->
            nullableNewFilter?.let { newFilter ->
                viewModel.filter = newFilter
                viewModel.updateData()
            }
        }
        (activity as MainActivity).loggedIn.observe(viewLifecycleOwner) {
            viewModel.updateData()
        }
        spinner.reloadNeeded.observe(viewLifecycleOwner) {
            viewModel.updateData()
        }
        spinner.navigateBack.observe(viewLifecycleOwner) {
            (activity as MainActivity).navigateOffline()
        }
    }

}
