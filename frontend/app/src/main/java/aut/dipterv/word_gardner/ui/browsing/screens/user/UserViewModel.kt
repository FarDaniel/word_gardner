package aut.dipterv.word_gardner.ui.browsing.screens.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aut.dipterv.word_gardner.local_data.models.Folder
import aut.dipterv.word_gardner.local_data.models.Pack
import aut.dipterv.word_gardner.network_data.interactors.FolderInteractor
import aut.dipterv.word_gardner.network_data.interactors.PackInteractor
import aut.dipterv.word_gardner.network_data.interactors.UserInteractor
import aut.dipterv.word_gardner.network_data.interactors.VoteInteractor
import aut.dipterv.word_gardner.network_data.models.UserModel
import aut.dipterv.word_gardner.network_data.models.VoteModel
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val packInteractor: PackInteractor,
    private val folderInteractor: FolderInteractor,
    private val userInteractor: UserInteractor,
    private val voteInteractor: VoteInteractor
) : ViewModel() {

    companion object {
        const val PREVIEW_LIMIT = 10
        const val NO_OFFSET = 0
    }

    val packs = MutableLiveData<List<Pack>>()
    val folders = MutableLiveData<List<Folder>>()
    val user = MutableLiveData<UserModel>()

    val ownVote = MutableLiveData<VoteModel?>()
    val upVoteCnt = MutableLiveData<Int>()
    val downVoteCnt = MutableLiveData<Int>()

    val login = MutableLiveData<Unit>()
    val fault = MutableLiveData<Unit>()

    fun getPacks(filter: SearchFilter) {
        val disposable =
            packInteractor.getPacksByFilter(filter, PREVIEW_LIMIT, NO_OFFSET).subscribe({
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

    fun getFolders(filter: SearchFilter) {
        val disposable =
            folderInteractor.getFoldersByFilter(filter, PREVIEW_LIMIT, NO_OFFSET).subscribe({
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

    fun getUser(filter: SearchFilter) {
        filter.userId?.let {
            val disposable = userInteractor.getUser(filter.userId).subscribe({ model ->
                model?.let { downloadedUser ->
                    user.value = downloadedUser
                    getOwnVote()
                }
            }, {
                handleException(it)
            })
        }
    }

    fun getVotes() {
        val votesDisposable = voteInteractor.getVotesByUser(user.value?.id ?: 0).subscribe({
            upVoteCnt.value = it.upVotes
            downVoteCnt.value = it.downVotes
        }, {
            handleException(it)
        })
    }

    fun getOwnVote() {
        val votesDisposable = voteInteractor.getVoteByUser(user.value?.id ?: -1).subscribe({
            ownVote.value = it
        }, {
            if (it.message?.contains("End") == true) {
                ownVote.value = null
            }
            handleException(it)
        })
    }

    fun vote(isUpVoted: Boolean, isDownVoted: Boolean) {
        if (isUpVoted || isDownVoted) {
            val voteDisposable = voteInteractor.addVote(
                VoteModel(
                    id = -1,
                    fromUser = -1,
                    toUser = user.value?.id ?: -1,
                    isUpvote = isUpVoted,
                    onFolder = null,
                    onPack = null
                )
            ).subscribe({
                updateVotes()
            }, {
                handleException(it)
            })
        } else {
            val deleteDisposable = voteInteractor.deleteVote(
                VoteModel(
                    id = -1,
                    fromUser = -1,
                    toUser = user.value?.id ?: -1,
                    isUpvote = false,
                    onFolder = null,
                    onPack = null
                )
            ).subscribe({
                updateVotes()
            }, {
                handleException(it)
            })
        }
    }

    fun updateVotes() {
        getOwnVote()
        getVotes()
    }

    private fun handleException(exception: Throwable) {
        try {
            if ((exception as HttpException).code() == 401) {
                login.value = Unit
            } else {
                fault.value = Unit
            }
        } catch (e: java.lang.Exception) {
            fault.value = Unit
        }
    }

    fun updateData(filter: SearchFilter) {
        getPacks(filter)
        getFolders(filter)
        getUser(filter)
    }
}
