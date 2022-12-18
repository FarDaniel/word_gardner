package aut.dipterv.word_gardner.ui.practice.screens.edit_pack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.MainActivity
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.FragmentEditPackBinding
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor
import aut.dipterv.word_gardner.ui.practice.screens.edit_pack.new_word_recycler_view.NewWordAdapter
import aut.dipterv.word_gardner.ui.practice.screens.edit_pack.new_word_recycler_view.NewWordModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.widget_edit_pack_footer.view.*
import java.util.*

@AndroidEntryPoint
class EditPackFragment : Fragment() {
    private lateinit var binding: FragmentEditPackBinding
    private lateinit var navController: NavController
    private lateinit var mainActivity: MainActivity
    val viewModel: EditPackViewModel by viewModels()
    private lateinit var wordsRecyclerView: RecyclerView
    private val args: EditPackFragmentArgs by navArgs()
    private var adapter = NewWordAdapter()
    private var isNativeToAdd = true

    var loggedInAction: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPackBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
        binding.newPackHeader.setFragment(this)
        mainActivity.setActiveViewModel(viewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordsRecyclerView = binding.wordsRecyclerview
        navController = this.findNavController()

        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true

        wordsRecyclerView.layoutManager = layoutManager

        viewModel.setPackFromDao(args.packId)

        makeListeners()

    }

    override fun onResume() {
        super.onResume()

        binding.footer.appear()
    }

    private fun savePack() {
        if (adapter.itemCount == 0) {
            viewModel.popupErrorMessage(
                "A pakli nem kerül mentésre kártyák nélkül, biztosan kilépsz?",
                false
            )
            return
        }
        if (binding.newPackHeader.packName.isEmpty() || binding.newPackHeader.categoryName.isEmpty()) {
            viewModel.popupErrorMessage(
                "A pakli nem kerül mentésre se pakli, se kategória név nélkül, biztosan kilépsz?",
                false
            )
            return
        }
        context?.let { context ->
            viewModel.savePack(
                context = context,
                categoryText = binding.newPackHeader.categoryName.lowercase(Locale.ROOT),
                packNameText = binding.newPackHeader.packName.lowercase(Locale.ROOT),
                difficulty = binding.footer.packDifficulty,
                cards = adapter.getItems()
            )
        }
    }

    fun addWord() {
        adapter.addItem(NewWordModel("", "", CardColor.GRAY))
        wordsRecyclerView.adapter = adapter
    }

    private fun makeListeners() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            val categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_category, categories)
            binding.newPackHeader.setCategoryAdapter(categoryAdapter)

            binding.footer.appear()
        }
        viewModel.newWord.observe(viewLifecycleOwner) { newWord ->
            adapter.addItem(newWord)
            wordsRecyclerView.adapter = adapter
        }
        viewModel.isPopupNeeded.observe(viewLifecycleOwner) { isNeeded ->
            mainActivity.setActiveViewModel(viewModel)
            mainActivity.setPopupState(isNeeded)
            binding.executePendingBindings()
        }
        viewModel.categoryText.observe(viewLifecycleOwner) { category ->
            binding.newPackHeader.categoryName = category
            binding.executePendingBindings()
        }
        viewModel.packNameText.observe(viewLifecycleOwner) { packName ->
            binding.newPackHeader.packName = packName
            binding.executePendingBindings()
        }
        viewModel.toPackSelector.observe(viewLifecycleOwner) {
            navController.navigate(
                EditPackFragmentDirections.actionEditPackFragmentToPackSelectorFragment()
            )
        }
        viewModel.diff.observe(viewLifecycleOwner) { difficulty ->
            binding.footer.setDifficulty(difficulty)
        }
        viewModel.loginNeeded.observe(viewLifecycleOwner) {
            loggedInAction = it
            mainActivity.popupLogin()
        }
        viewModel.fault.observe(viewLifecycleOwner) {
            viewModel.popupErrorMessage(
                "Valamilyen hiba történt a feltöltéssel, ki szeretnél lépni online megosztás nélkül?",
                true
            )
        }
        viewModel.navigateForward.observe(viewLifecycleOwner) {
            navController.navigate(EditPackFragmentDirections.actionEditPackFragmentToPackSelectorFragment())
        }
        (activity as MainActivity).loggedIn.observe(viewLifecycleOwner) {
            loggedInAction?.invoke()
        }
        binding.footer.save.observe(viewLifecycleOwner) {
            savePack()
        }
        binding.footer.delete.observe(viewLifecycleOwner) {
            viewModel.popupErrorMessage(
                "Biztosan törölni szeretnéd az összeállított szókészletet?",
                false
            )
        }
        binding.footer.makeWords.observe(viewLifecycleOwner) {
            viewModel.autoMakeNewWords(binding.footer.words_text.text.toString(), isNativeToAdd)
        }
        binding.footer.isPublic.observe(viewLifecycleOwner) {
            viewModel.isPublic = it
        }
        binding.footer.isExpanded.observe(viewLifecycleOwner) {
            if (viewModel.onlineId != null) {
                if (binding.footer.isPublic.value == true) {
                    binding.footer.switchPublic()
                }
                viewModel.isPublic = false
                binding.footer.lock()
            }
        }

    }

}
