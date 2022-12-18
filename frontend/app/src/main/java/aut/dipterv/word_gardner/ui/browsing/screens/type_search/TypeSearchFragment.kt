package aut.dipterv.word_gardner.ui.browsing.screens.type_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import aut.dipterv.word_gardner.MainActivity
import aut.dipterv.word_gardner.databinding.FragmentTypeSearchBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.widgets.shared_widgets.SearchSwipeScreenWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.widget_number_picker_popup.view.*

@AndroidEntryPoint
class TypeSearchFragment : Fragment(), SearchPackNavigator {
    val viewModel: TypeSearchViewModel by viewModels()
    private lateinit var binding: FragmentTypeSearchBinding
    private lateinit var navController: NavController
    private val args: TypeSearchFragmentArgs by navArgs()
    private lateinit var swipeScreen: SearchSwipeScreenWidget
    private var searchFilter: SearchFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypeSearchBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.type = args.searchableType
        viewModel.filter = args.filter
        Log.d(
            "MAKING",
            "namePart = " + args.filter.namePart + "\n"
                    + "categoryPart = " + args.filter.categoryPart + "\n"
                    + "minimalUpvotePercentage = " + args.filter.minimalUpvotePercentage + "\n"
                    + "userId = " + args.filter.userId + "\n"
                    + "easyIncluded = " + args.filter.easyIncluded + "\n"
                    + "mediumIncluded = " + args.filter.mediumIncluded + "\n"
                    + "hardIncluded = " + args.filter.hardIncluded
        )
        swipeScreen = binding.searchSwipeScreenWidget
        viewModel.init()
        binding.fragment = this
        initListeners()
        navController = findNavController()
    }

    fun openNumberPicker() {
        binding.typeSearchExtendableNumberPicker.popup((viewModel.actualPage.value ?: 0) + 1)
    }

    override fun onResume() {
        super.onResume()

        binding.spinner.reset()
    }

    private fun initListeners() {
        swipeScreen.leftContentNeeded.observe(viewLifecycleOwner) {
            viewModel.getPrevious()
        }
        swipeScreen.rightContentNeeded.observe(viewLifecycleOwner) {
            viewModel.getNext()
        }
        binding.typeSearchExtendableNumberPicker.chosenNumber.observe(viewLifecycleOwner) {
            viewModel.jumpTo((it ?: 1) - 1)
        }
        binding.spinner.reloadNeeded.observe(viewLifecycleOwner) {
            viewModel.jumpTo(0)
        }
        binding.spinner.navigateBack.observe(viewLifecycleOwner) {
            (activity as MainActivity).navigateOffline()
        }
        viewModel.previousModels.observe(viewLifecycleOwner) { models ->
            swipeScreen.setLeft(models)
        }
        viewModel.nextModels.observe(viewLifecycleOwner) { models ->
            swipeScreen.setRight(models)
        }
        viewModel.initialModels.observe(viewLifecycleOwner) { models ->
            swipeScreen.init(
                models ?: arrayListOf(),
                viewModel.type,
                this
            )
        }
        viewModel.allPage.observe(viewLifecycleOwner) {
            val allPages = (it ?: 1)
            binding.allPagesTextview.text = allPages.toString()
            binding.typeSearchExtendableNumberPicker.numberPicker.maxValue = allPages
        }
        viewModel.actualPage.observe(viewLifecycleOwner) {
            val actualPage = ((it ?: 0) + 1)
            binding.actualPageTextview.text = actualPage.toString()
            binding.typeSearchExtendableNumberPicker.numberPicker.value = actualPage
        }
        viewModel.toJumpMainModels.observe(viewLifecycleOwner) {
            swipeScreen.loadMainPage(it ?: listOf())
        }
        viewModel.fault.observe(viewLifecycleOwner) {
            binding.spinner.popup("Hálózati hiba történt, próbáld újra később.")
        }
        viewModel.successfulCall.observe(viewLifecycleOwner) {
            binding.spinner.disappear()
        }
    }

    override fun navigateToCategorySearch(
        type: SearchableModelBase.SearchableType
    ) {
        throw Error("Already here!")
    }

    override fun navigateToUserDetails(userId: Long) {
        navController.navigate(
            TypeSearchFragmentDirections.actionTypeSearchFragmentToUserFragment(
                searchFilter?.copy(userId = userId) ?: SearchFilter(userId = userId)
            )
        )
    }

    override fun navigateDownloadDetails(type: SearchableModelBase.SearchableType, id: Long) {
        navController.navigate(
            TypeSearchFragmentDirections.actionTypeSearchFragmentToDownloadDetailsFragment(type, id)
        )
    }
}
