package aut.dipterv.word_gardner.ui.practice.screens.pack_selector

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.dipterv.word_gardner.interfaces.PopupReadyViewModel
import aut.dipterv.word_gardner.local_data.dao.WordPackDao
import aut.dipterv.word_gardner.local_data.models.Folder
import aut.dipterv.word_gardner.local_data.relations.folder_pack.PackFolderCrossRef
import aut.dipterv.word_gardner.network_data.interactors.FolderInteractor
import aut.dipterv.word_gardner.network_data.interceptors.SessionManager
import aut.dipterv.word_gardner.network_data.models.FolderModel
import aut.dipterv.word_gardner.network_data.models.PackFolderCrossRefModel
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.landing_recycler_view.LandingModel
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PackSelectorViewModel @Inject constructor(
    private val dao: WordPackDao,
    private val folderInteractor: FolderInteractor
) : ViewModel(),
    PopupReadyViewModel {

    override var questionText: String = ""
    override var isPopupNeeded = MutableLiveData(false)
    val categories = MutableLiveData<List<String>>()
    val rows = MutableLiveData<MutableCollection<LandingModel>>()
    val folderToRemove = MutableLiveData<SearchableModelMultiple>()
    private val folders = ArrayList<Folder>()
    var activeFolderModel: SearchableModelMultiple? = null

    val loginNeeded = MutableLiveData<() -> Unit>()

    // Is faulted under uploading
    val fault = MutableLiveData<Boolean>()
    var isOnline = true

    private fun loadCategories() {
        viewModelScope.launch {
            val list = ArrayList<String>()
            list.addAll(dao.getPackCategories())
            list.addAll(dao.getFolderCategories())
            categories.value = list.distinct()
        }
    }

    fun activateFolder(folderModel: SearchableModelMultiple) {
        activeFolderModel = folderModel
    }

    fun loadPacks() {
        viewModelScope.launch {
            val cardsFromDao = dao.getPacks()
            val foldersFromDao = dao.getFolders()
            val categories: HashMap<String, LandingModel> = HashMap()
            cardsFromDao.forEach {
                if (categories.containsKey(it.category)) {
                    categories[it.category]?.packs?.add(SearchableModelSingle(it))
                } else {
                    val landingModel = LandingModel(it.category)
                    landingModel.packs.add(SearchableModelSingle(it))
                    categories[it.category] = landingModel
                }
            }
            folders.addAll(foldersFromDao)
            foldersFromDao.forEach { folder ->
                val model = SearchableModelMultiple(
                    folder
                )
                val packIds = dao.getPackIdsFromFolder(folder.folderId)
                val singleModels = ArrayList<SearchableModelSingle>()
                if (packIds.isNotEmpty()) {
                    packIds.forEach { id ->
                        categories.values.forEach { landingModel ->
                            for (i in 0 until landingModel.packs.size) {
                                if (landingModel.packs[i].type == SearchableModelBase.SearchableType.TYPE_SINGLE)
                                    if (id == (landingModel.packs[i] as SearchableModelSingle).pack.packId)
                                        singleModels.add((landingModel.packs[i] as SearchableModelSingle))
                            }
                        }
                    }
                }
                model.setPacks(singleModels)
                if (model.packs.isNotEmpty()) {
                    if (categories.containsKey(model.folder.category)) {
                        categories[model.folder.category]?.packs?.add(model)
                    } else {
                        val landingModel = LandingModel(model.folder.category)
                        landingModel.packs.add(model)
                        categories[model.folder.category] = landingModel
                    }

                } else {
                    dao.deleteFolder(model.folder.folderId)
                }
            }
            rows.value = categories.values
            loadCategories()
        }
    }

    fun updateFolderInDao(folderModel: SearchableModelMultiple) {
        viewModelScope.launch {
            val folderIterator = folders.iterator()
            var updatedFolder = Folder(
                folderId = -1,
                category = "",
                folderName = "",
                difficulty = folderModel.difficulty
            )
            for (folder in folderIterator) {
                if (folder.category == activeFolderModel?.folder?.category && folder.folderName == activeFolderModel?.title) {
                    updatedFolder = Folder(
                        folderId = folder.folderId,
                        category = folderModel.folder.category,
                        folderName = folderModel.title,
                        difficulty = folderModel.difficulty,
                        onlineId = folderModel.onlineId
                    )

                    dao.insertFolder(updatedFolder)
                    folderIterator.remove()
                    dao.deletePackFolderCrossRefsOfFolder(updatedFolder.folderId)
                    folderModel.packs.forEach {
                        dao.insertPackFolderCrossRef(
                            PackFolderCrossRef(
                                updatedFolder.folderId,
                                it.pack.packId
                            )
                        )
                    }
                }
            }
            if (updatedFolder.folderId != -1L)
                folders.add(updatedFolder)
        }
        loadCategories()
    }

    fun saveFolderToDao(folderModel: SearchableModelMultiple) {
        val folder = Folder(
            onlineId = folderModel.folder.onlineId,
            category = folderModel.folder.category,
            folderName = folderModel.title,
            difficulty = folderModel.difficulty
        )

        viewModelScope.launch {
            val folderId = dao.insertFolder(folder)
            folders.add(
                Folder(
                    folderId = folderId,
                    onlineId = folder.onlineId,
                    category = folder.category,
                    difficulty = folder.difficulty,
                    folderName = folder.folderName
                )
            )
            folderModel.packs.forEach {
                dao.insertPackFolderCrossRef(PackFolderCrossRef(folderId, it.pack.packId))
            }
        }
        loadCategories()
    }

    fun updateIsOnline(context: Context) {
        val sessionManager = SessionManager(context)
        isOnline = sessionManager.fetchIsOnline()
    }

    fun saveFolder(context: Context, folder: SearchableModelMultiple) {
        val folderToBackend = FolderModel(
            id = folder.folder.onlineId ?: -1,
            userId = -1,
            upVotes = 0,
            downVotes = 0,
            category = folder.folder.category,
            folderName = folder.folder.folderName,
            difficulty = folder.difficulty.toString().lowercase(Locale.ROOT)
        )
        val folderDisposable = folderInteractor.addFolder(folderToBackend)
            .subscribe({ onlineFolderId ->
                viewModelScope.launch {
                    val newFolder =
                        SearchableModelMultiple(folder.folder.copy(onlineId = onlineFolderId))
                    newFolder.setPacks(folder.packs)
                    saveFolderToDao(newFolder)
                    folder.packs.forEach {
                        val actualPack = it.pack
                        val sessionManager = SessionManager(context)
                        if (actualPack.onlineId != null && sessionManager.fetchIsOnline()) {
                            val crossRefDisposable = folderInteractor.addPackFolderCrossRef(
                                PackFolderCrossRefModel(
                                    id = -1,
                                    packId = actualPack.onlineId,
                                    folderId = onlineFolderId
                                )
                            ).subscribe({ id ->
                            }, {
                                folderInteractor.deleteFolder(onlineFolderId)
                                handleException(it, true) { saveFolder(context, folder) }
                            })
                        }
                    }
                }
            }, {
                handleException(it, true) { saveFolder(context, folder) }
            })
    }

    fun popupErrorMessage(context: Context, text: String) {
        val sessionManager = SessionManager(context)
        isOnline = sessionManager.fetchIsOnline()
        questionText = text
        isPopupNeeded.value = true
    }

    override fun accepted() {
        viewModelScope.launch {
            folders.clear()
            folders.addAll(dao.getFolders())
            folders.forEach { folder ->
                if (folder.category == activeFolderModel?.folder?.category && folder.folderName == activeFolderModel?.title) {
                    dao.deleteFolder(folder.folderId)
                    deleteFolderFromServer(folder)
                    folderToRemove.value = activeFolderModel
                }
            }
        }
        isPopupNeeded.value = false
        loadCategories()
    }

    private fun deleteFolderFromServer(folder: Folder) {
        if (folder.onlineId != null && isOnline) {
            val folderDisposable = folderInteractor.deleteFolder(folder.onlineId).subscribe({}, {
                handleException(it, false) {
                    deleteFolderFromServer(folder)
                }
            })
        }
    }

    private fun handleException(exception: Throwable, isCreation: Boolean, afterLogin: () -> Unit) {
        try {
            if ((exception as HttpException).code() == 401) {
                loginNeeded.value = afterLogin
            } else {
                fault.value = isCreation
            }
        } catch (e: java.lang.Exception) {
            fault.value = isCreation
        }
    }

}
