package aut.dipterv.word_gardner.ui.practice.screens.loaded_packs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.MainActivity
import aut.dipterv.word_gardner.databinding.FragmentLoadedPackBinding
import aut.dipterv.word_gardner.interfaces.ActivePacksReader
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardAdapter
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadedPacksFragment : Fragment(), ActivePacksReader {
    val viewModel: LoadedPacksViewModel by viewModels()
    private lateinit var binding: FragmentLoadedPackBinding
    private lateinit var recyclerView: RecyclerView
    private val adapter = WordCardAdapter()
    lateinit var mainActivity: MainActivity
    override var navController: NavController? = null
    override var fallbackDirection =
        LoadedPacksFragmentDirections.actionLandingPageFragmentToPackSelectorFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadedPackBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView

        navController = this.findNavController()

        recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        makeListeners()
        context?.let { context ->
            val activePacks = getActivePacks(context)
            viewModel.getCardsFromDao(activePacks)
        }
    }

    private fun makeListeners() {
        viewModel.cardList.observe(viewLifecycleOwner) { cards ->
            adapter.clear()
            cards.forEach { card ->
                val model =
                    WordCardModel(
                        foreignWord = card.foreignWord,
                        nativeWord = card.nativeWord,
                        backGround = card.backGround
                    )
                adapter.addItem(model)
            }
            recyclerView.adapter = adapter
            binding.executePendingBindings()
        }
        viewModel.navigateToFallback.observe(viewLifecycleOwner) {
            navController?.navigate(fallbackDirection)
        }
    }

    fun selectPacksButtonClicked() {
        navController?.navigate(
            LoadedPacksFragmentDirections.actionLandingPageFragmentToPackSelectorFragment()
        )
    }

    fun navigateToSettings() {
        navController?.navigate(
            LoadedPacksFragmentDirections.actionLandingPageFragmentToSettingsFragment()
        )
    }

    fun cleverStartIconPressed() {
        navController?.navigate(
            LoadedPacksFragmentDirections.actionLandingPageFragmentToTestFragment(
                hint = binding.inputToHintWidget.getHintList().toIntArray(),
                input = binding.inputToHintWidget.getInputList().toIntArray(),
                recommended = true
            )
        )
    }

    fun startIconPressed() {
        navController?.navigate(
            LoadedPacksFragmentDirections.actionLandingPageFragmentToTestFragment(
                hint = binding.inputToHintWidget.getHintList().toIntArray(),
                input = binding.inputToHintWidget.getInputList().toIntArray(),
                recommended = false
            )
        )
    }
}
