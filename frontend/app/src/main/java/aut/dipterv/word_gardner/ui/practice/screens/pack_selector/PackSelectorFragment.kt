package aut.dipterv.word_gardner.ui.practice.screens.pack_selector

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.MainActivity
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.FragmentPackSelectorBinding
import aut.dipterv.word_gardner.interfaces.PackSelectorInterface
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty
import aut.dipterv.word_gardner.local_data.models.Folder
import aut.dipterv.word_gardner.network_data.interceptors.SessionManager
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view.adapter.LandingAdapter
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.widget_pack_selector_footer.view.*

@AndroidEntryPoint
class PackSelectorFragment : Fragment(), PackSelectorInterface {

    val viewModel: PackSelectorViewModel by viewModels()
    lateinit var binding: FragmentPackSelectorBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var navController: NavController
    private lateinit var adapter: LandingAdapter
    private lateinit var packsRecyclerView: RecyclerView

    var loggedInAction: (() -> Unit)? = null

    override val activePacks: ArrayList<SearchableModelSingle> = ArrayList()
    override var pickingActive = MutableLiveData(false)
    override val packsActive = MutableLiveData(false)
    override val editingFolder = MutableLiveData(false)
    override lateinit var lifecycleOwner: LifecycleOwner

    companion object {
        const val ACTIVE_PACK_IDS = "active_pack_ids"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPackSelectorBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
        mainActivity.setActiveViewModel(viewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LandingAdapter(view.context, this)
        packsRecyclerView = binding.packsRecyclerview
        navController = this.findNavController()

        val layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        packsRecyclerView.layoutManager = layoutManager

        viewModel.loadPacks()
        makeListeners()
    }

    override fun onResume() {
        super.onResume()

        binding.footer.appear()
        context?.let { context ->
            viewModel.updateIsOnline(context)
        }
    }

    override fun navigateToTestFragment(packs: List<SearchableModelSingle>) {
        deselectAllPacks()
        loadPacks(packs)
        navController.navigate(
            PackSelectorFragmentDirections.actionPackSelectorFragmentToWordCardFragment()
        )
    }

    override fun navigateToEditPackFragment(model: SearchableModelSingle) {
        deselectAllPacks()
        navController.navigate(
            PackSelectorFragmentDirections.actionPackSelectorFragmentToEditPackFragment(
                model.pack.packId
            )
        )
    }

    override fun lastPackDeactivated() {
        packsActive.value = false
        pickingActive.value = editingFolder.value
        if (editingFolder.value == false) {
            binding.footer.collapse()
            updatePicking()
        }
    }

    override fun editFolder(folder: SearchableModelMultiple) {
        if (viewModel.activeFolderModel != folder) {
            viewModel.activateFolder(folder)
            binding.footer.setFolderName(folder.title)
            binding.footer.setCategory(folder.folder.category)
            switchColorStateOfPacks(true)
            deselectAllPacks()
            selectPacks(folder.packs)
            editingFolder.value = true
        } else {
            viewModel.activeFolderModel = null
            switchColorStateOfPacks(false)
            deselectAllPacks()
            viewModel.activeFolderModel?.color?.value =
                viewModel.activeFolderModel?.difficulty?.colorCode
            editingFolder.value = false
            binding.footer.setFolderName("")
            binding.footer.setCategory("")
        }
        updatePicking()
    }

    override fun switchColorStateOfPacks(inEditMode: Boolean) {
        if (inEditMode) {
            adapter.getItems().forEach { row ->
                row.packs.forEach { model ->
                    model.color.value = Difficulty.UNDEFINED.colorCode
                }
            }
        } else {
            adapter.getItems().forEach { row ->
                row.packs.forEach { model ->
                    model.color.value = model.difficulty.colorCode
                }
            }
        }
    }

    override fun deselectAllPacks() {
        adapter.getItems().forEach { row ->
            row.packs.forEach { model ->
                SearchableModelBase.SearchableType.TYPE_SINGLE
                model.picked.value = false
            }
        }
        activePacks.clear()
        lastPackDeactivated()
    }

    override fun selectPacks(packs: List<SearchableModelSingle>) {
        packs.forEach { pack ->
            adapter.getSingleItem(pack.pack.packId)?.picked?.value = true
        }
        packsActive.value = packs.isNotEmpty()
        activePacks.addAll(packs)
    }

    override fun deselectPacks(packs: List<SearchableModelSingle>) {
        packs.forEach { pack ->
            adapter.getSingleItem(pack.pack.packId)?.picked?.value = false
        }
        activePacks.removeAll(packs)
        if (activePacks.size == 0) {
            lastPackDeactivated()
        }
    }

    fun loadActives() {
        loadPacks(activePacks)
        deselectAllPacks()
        navController.navigate(
            PackSelectorFragmentDirections.actionPackSelectorFragmentToWordCardFragment()
        )
    }

    fun makeNewPack() {
        navController.navigate(
            PackSelectorFragmentDirections.actionPackSelectorFragmentToEditPackFragment()
        )
    }

    private fun updatePicking() {
        if (binding.footer.isExpanded.value == true) {
            pickingActive.value = true
        } else {
            pickingActive.value = activePacks.size > 0
        }
    }

    private fun makeListeners() {
        viewModel.isPopupNeeded.observe(viewLifecycleOwner) { isNeeded ->
            mainActivity.setActiveViewModel(viewModel)
            mainActivity.setPopupState(isNeeded)
            binding.executePendingBindings()
            binding.footer.setFolderName("")
            binding.footer.setCategory("")
        }
        viewModel.rows.observe(viewLifecycleOwner) { rows ->

            adapter.clear()

            rows.forEach {
                adapter.addItem(it)
            }

            packsRecyclerView.adapter = adapter
        }
        viewModel.folderToRemove.observe(viewLifecycleOwner) { model ->
            removeFolderFromScreen(model)

            packsRecyclerView.adapter = adapter
            editingFolder.value = false
            deselectAllPacks()
            switchColorStateOfPacks(false)
        }
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            val categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_category, categories)
            binding.footer.category_name_text.setAdapter(categoryAdapter)
            binding.executePendingBindings()
            binding.footer.appear()
        }

        viewModel.loginNeeded.observe(viewLifecycleOwner) {
            loggedInAction = it
            mainActivity.popupLogin()
        }
        viewModel.fault.observe(viewLifecycleOwner) {
            context?.let { context ->
                if (it) {
                    viewModel.popupErrorMessage(
                        context,
                        "Nem sikerült feltölteni a mappát, szeretnéd törölni?"
                    )
                } else {
                    viewModel.popupErrorMessage(
                        context,
                        "Nem sikerült a szerverről törölni a mappát, fent maradhat a szerveren?\n\n" +
                                "(Ilyenkor letöltve majd újra törölve újra " +
                                "próbálható a szerverről való eltávolítás)"
                    )
                }
            }
        }
        binding.footer.selector_flip_button.firstSideClicked.observe(viewLifecycleOwner) {
            context?.let { context ->
                if (SessionManager(context).fetchIsOnline()) {
                    navController.navigate(
                        PackSelectorFragmentDirections.actionPackSelectorFragmentToSearchFragment()
                    )
                } else {
                    viewModel.popupErrorMessage(
                        context, "Offline módban nem lehet böngészni a hálózaton.\n\n" +
                                "Ha ki szeretnéd kapcsolni, azt a beállításokban teheted meg."
                    )
                }
            }
        }
        binding.footer.selector_flip_button.secondSideClicked.observe(viewLifecycleOwner) {
            loadActives()
        }
        binding.footer.updateFolder.observe(viewLifecycleOwner) {
            if (packsActive.value == true) {
                val model = SearchableModelMultiple(
                    Folder(
                        category = binding.footer.category_name_text.text.toString(),
                        folderName = binding.footer.pack_text.text.toString()
                    )
                )
                model.setPacks(activePacks)
                adapter.getItems().forEach { row ->
                    row.packs.forEach { model ->
                        if (model.type == SearchableModelBase.SearchableType.TYPE_SINGLE)
                            (model as SearchableModelSingle).picked.value = false
                    }
                }
                viewModel.activeFolderModel?.let {
                    removeFolderFromScreen(it)
                }
                adapter.addPackModel(model)

                viewModel.updateFolderInDao(model)

                binding.footer.setFolderName("")
                binding.footer.setCategory("")
                activePacks.clear()
                packsActive.value = false
                pickingActive.value = false

                deselectAllPacks()

                switchColorStateOfPacks(false)
                deselectAllPacks()
                editingFolder.value = false

                packsRecyclerView.adapter = adapter
                binding.executePendingBindings()
            } else {
                viewModel.isPopupNeeded.value = true
                switchColorStateOfPacks(false)
                deselectAllPacks()
                editingFolder.value = false
                binding.footer.setFolderName("")
                binding.footer.setCategory("")
            }
            binding.footer.collapse()
            updatePicking()
        }
        binding.footer.delete.observe(viewLifecycleOwner) {
            binding.footer.collapse()
            context?.let { context ->
                viewModel.popupErrorMessage(context, "Biztosan szeretnéd törölni a mappát?")
            }
            updatePicking()
        }
        binding.footer.makeFolder.observe(viewLifecycleOwner) {
            if (packsActive.value == true) {
                val model = SearchableModelMultiple(
                    Folder(
                        category = binding.footer.category_name_text.text.toString(),
                        folderName = binding.footer.pack_text.text.toString()
                    )
                )
                model.setPacks(activePacks)
                adapter.addPackModel(model)

                if (binding.footer.isPublic) {
                    context?.let { context ->
                        viewModel.saveFolder(context, model)
                    }
                } else {
                    viewModel.saveFolderToDao(model)
                }

                binding.footer.setFolderName("")
                binding.footer.setCategory("")
                activePacks.clear()
                packsActive.value = false
                pickingActive.value = false

                deselectAllPacks()

                packsRecyclerView.adapter = adapter
                binding.executePendingBindings()
            }
            binding.footer.collapse()
            updatePicking()
        }
        binding.footer.makePack.observe(viewLifecycleOwner) {
            makeNewPack()
        }
        binding.footer.isExpanded.observe(viewLifecycleOwner) {
            updatePicking()
        }
        editingFolder.observe(viewLifecycleOwner) { editing ->
            binding.footer.changeMode(
                editing,
                packsActive.value ?: false
            )
        }
        packsActive.observe(viewLifecycleOwner) { active ->
            binding.footer.changeMode(
                editingFolder.value ?: false,
                active
            )
        }
    }

    private fun loadPacks(packs: List<SearchableModelSingle>) {
        val args = ArrayList<Long>()
        for (model in packs) {
            args.add(model.pack.packId)
        }
        context?.let { context ->
            val editor: SharedPreferences.Editor =
                context.getSharedPreferences(ACTIVE_PACK_IDS, Context.MODE_PRIVATE).edit()
            editor.putString(ACTIVE_PACK_IDS, args.toString())
            editor.apply()
        }
    }

    private fun removeFolderFromScreen(model: SearchableModelMultiple) {
        val itemsIterator = adapter.getItems().iterator()
        for (item in itemsIterator) {
            if (item.title == model.folder.category) {
                val packsIterator = item.packs.iterator()
                for (pack in packsIterator) {
                    if (pack.title == model.title) {
                        if (item.packs.size == 1) {
                            itemsIterator.remove()
                        } else {
                            packsIterator.remove()
                        }
                    }
                }
            }
        }
    }

}
