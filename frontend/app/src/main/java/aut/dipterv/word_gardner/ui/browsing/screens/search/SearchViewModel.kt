package aut.dipterv.word_gardner.ui.browsing.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aut.dipterv.word_gardner.local_data.models.Folder
import aut.dipterv.word_gardner.local_data.models.Pack
import aut.dipterv.word_gardner.local_data.models.User
import aut.dipterv.word_gardner.network_data.interactors.FolderInteractor
import aut.dipterv.word_gardner.network_data.interactors.PackInteractor
import aut.dipterv.word_gardner.network_data.interactors.UserInteractor
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val packInteractor: PackInteractor,
    private val folderInteractor: FolderInteractor,
    private val userInteractor: UserInteractor
) : ViewModel() {

    val packs = MutableLiveData<List<Pack>>()
    val folders = MutableLiveData<List<Folder>>()
    val users = MutableLiveData<List<User>>()

    val fault = MutableLiveData<Unit>()
    val login = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    var filter = SearchFilter()

    companion object {
        const val LIMIT = 10
        const val NO_OFFSET = 0
    }

    fun getPacks() {
        val disposable = packInteractor.getPacksByFilter(filter, LIMIT, NO_OFFSET).subscribe({
            it?.let { downloadedPackList ->
                val convertedPacks = ArrayList<Pack>()
                downloadedPackList.forEach { pack ->
                    convertedPacks.add(pack.toLocal())
                }
                packs.value = convertedPacks
            }
        }, {
            handleException(it)
        })
    }

    fun getFolders() {
        val disposable = folderInteractor.getFoldersByFilter(filter, LIMIT, NO_OFFSET).subscribe({
            it?.let { downloadedFolderList ->
                val convertedFolders = ArrayList<Folder>()
                downloadedFolderList.forEach { folder ->
                    convertedFolders.add(folder.toLocal())
                }
                folders.value = convertedFolders
            }
        }, {
            handleException(it)
        })
    }

    fun getUsers() {
        val disposable = userInteractor.getUsersByFilter(filter, LIMIT, NO_OFFSET).subscribe({
            it?.let { downloadedUserList ->
                val convertedUsers = ArrayList<User>()
                downloadedUserList.forEach { user ->
                    convertedUsers.add(user.toLocal())
                }
                users.value = convertedUsers
            }
        }, {
            handleException(it)
        })
    }

    fun updateData() {
        getFolders()
        getPacks()
        getUsers()
    }

    fun handleException(exception: Throwable) {
        try {
            if ((exception as HttpException).code() == 401) {
                login.tryEmit(Unit)
            } else {
                fault.value = Unit
            }
        } catch (e: java.lang.Exception) {
            fault.value = Unit
        }
    }

}
