package aut.dipterv.word_gardner.ui.browsing.screens.download_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.dipterv.word_gardner.local_data.dao.WordPackDao
import aut.dipterv.word_gardner.local_data.models.User
import aut.dipterv.word_gardner.local_data.relations.folder_pack.PackFolderCrossRef
import aut.dipterv.word_gardner.network_data.interactors.*
import aut.dipterv.word_gardner.network_data.models.VoteModel
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase.SearchableType.TYPE_CARD
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase.SearchableType.TYPE_SINGLE
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelCard
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class DownloadDetailsViewModel @Inject constructor(
    val folderInteractor: FolderInteractor,
    val userInteractor: UserInteractor,
    val packInteractor: PackInteractor,
    val cardInteractor: CardInteractor,
    val voteInteractor: VoteInteractor,
    val dao: WordPackDao
) : ViewModel() {
    val previousModels = MutableLiveData<List<SearchableModelBase>>()
    val nextModels = MutableLiveData<List<SearchableModelBase>>()
    val toJumpMainModels = MutableLiveData<List<SearchableModelBase>>()
    val initialModels = MutableLiveData<List<SearchableModelBase>>()
    val actualPage = MutableLiveData(0)
    val allPage = MutableLiveData(0)
    val user = MutableLiveData<User>()
    val name = MutableLiveData<String>()
    val downloaded = MutableLiveData<Unit>()
    val ownVote = MutableLiveData<VoteModel?>()

    val downVotes = MutableLiveData<Int>()
    val upVotes = MutableLiveData<Int>()
    val fault = MutableLiveData<Unit>()
    val login = MutableLiveData<Unit>()

    var toDownload = 0
    var isOwn = false

    var id = -1L
    lateinit var type: SearchableModelBase.SearchableType

    companion object {
        const val PAGE_SIZE = 6
    }

    fun init() {
        loadHeader()
        when (type) {
            TYPE_SINGLE -> {
                val getCntDisposable = packInteractor.getPackCntByFolder(id).subscribe({
                    allPage.value = ceil((it.count).toDouble() / PAGE_SIZE).toInt()
                    if ((allPage.value ?: 0) > PAGE_SIZE * 2) {
                        val packByFilterDisposable = packInteractor.getPacksByFolder(
                            id,
                            PAGE_SIZE * 2,
                            (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelSingle>()
                                updateVotes()
                                val lastPageDisposable = packInteractor.getPacksByFolder(
                                    id,
                                    PAGE_SIZE,
                                    (((allPage.value ?: 1) - 1)) * PAGE_SIZE
                                ).subscribe({ nullableLPList ->
                                    nullableLPList?.let { lPList ->
                                        lPList.forEach { model ->
                                            response.add(SearchableModelSingle(model.toLocal()))
                                        }
                                    }
                                    list.forEach { model ->
                                        response.add(SearchableModelSingle(model.toLocal()))
                                    }

                                    initialModels.value = response
                                }, {
                                    handleException(it)
                                })
                            }
                        }, {
                            handleException(it)
                        })
                    } else {
                        val packByFilterDisposable = packInteractor.getPacksByFolder(
                            id,
                            PAGE_SIZE * 2,
                            (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            updateVotes()
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelSingle>()

                                list.forEach { model ->
                                    response.add(SearchableModelSingle(model.toLocal()))
                                }

                                initialModels.value = response
                            }
                        }, {

                            handleException(it)
                        })
                    }
                }, {

                    handleException(it)
                })
            }
            TYPE_CARD -> {
                val getCntDisposable = cardInteractor.getWordCardCntByPack(id).subscribe({
                    allPage.value =
                        ceil((it.count).toDouble() / PAGE_SIZE).toInt()
                    if ((allPage.value ?: 0) > PAGE_SIZE * 2) {
                        val cardFilterDisposable = cardInteractor.getByPackId(
                            id,
                            PAGE_SIZE * 2,
                            (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            updateVotes()
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelCard>()
                                val lastPageDisposable = cardInteractor.getByPackId(
                                    id,
                                    PAGE_SIZE,
                                    (allPage.value ?: 0) - 1
                                ).subscribe({ nullableLPList ->
                                    nullableLPList?.let { lPList ->
                                        lPList.forEach { model ->
                                            response.add(SearchableModelCard(model.toLocal()))
                                        }
                                    }
                                    list.forEach { model ->
                                        response.add(SearchableModelCard(model.toLocal()))
                                    }

                                    initialModels.value = response
                                }, {
                                    handleException(it)
                                })
                            }
                        }, {
                            handleException(it)
                        })
                    } else {
                        val packByFilterDisposable = cardInteractor.getByPackId(
                            id,
                            PAGE_SIZE * 2,
                            (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            updateVotes()
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelCard>()

                                list.forEach { model ->
                                    response.add(SearchableModelCard(model.toLocal()))
                                }

                                initialModels.value = response
                            }
                        }, {
                            handleException(it)
                        })
                    }
                }, {
                    handleException(it)
                })
            }
        }
    }

    fun getPrevious() {
        actualPage.value = if ((actualPage.value ?: 0) == 0) {
            (allPage.value ?: 0) - 1
        } else {
            (actualPage.value ?: 0) - 1
        }
        loadPage(
            if (actualPage.value == 0) {
                ((allPage.value ?: 0) - 1)
            } else {
                ((actualPage.value ?: 0) - 1)
            },
            previousModels
        )
    }

    fun getNext() {
        actualPage.value = if ((actualPage.value ?: 0) == (allPage.value ?: 0) - 1) {
            0
        } else {
            (actualPage.value ?: 0) + 1
        }
        loadPage(
            if (actualPage.value == (allPage.value ?: 0) - 1) {
                0
            } else {
                ((actualPage.value ?: 0) + 1)
            },
            nextModels
        )

    }

    fun jumpTo(page: Int) {
        if (page > (allPage.value ?: 0)) {
            throw Exception("Impossible page number.")
        }

        actualPage.value = page
        loadPage(page, toJumpMainModels)
        loadPage(
            if (page == 0) {
                ((allPage.value ?: 0) - 1)
            } else {
                page - 1
            },
            previousModels
        )
        loadPage(
            if (page == (allPage.value ?: 0) - 1) {
                0
            } else {
                page + 1
            },
            nextModels
        )
    }

    fun download() {
        when (type) {
            TYPE_SINGLE -> {
                val folderRef = folderInteractor.getFolder(id).subscribe({ folderDto ->
                    isOwn = folderDto.isOwn ?: false
                    val folderSizeRef = packInteractor.getPackCntByFolder(id).subscribe({ packCnt ->
                        val packsRef =
                            packInteractor.getPacksByFolder(id, packCnt.count, 0)
                                .subscribe({ packs ->
                                    viewModelScope.launch {
                                        val folder = dao.getFolderByOnlineId(id)
                                        if (folder.isEmpty()) {
                                            val folder = folderDto.toLocal().copy(
                                                category = folderDto.category.lowercase(),
                                                folderName = folderDto.folderName.lowercase(),
                                                onlineId = folderDto.id
                                            )
                                            val folderId = dao.insertFolder(folder)
                                            toDownload = packs.size
                                            packs.forEach {
                                                downloadPack(it.id, folderId)
                                            }
                                        } else {
                                            dao.getPackIdsFromFolder(folder.first().folderId)
                                                .forEach {
                                                    dao.deleteWordCards(it)
                                                    dao.deletePack(it)
                                                }
                                            dao.deletePackFolderCrossRefsOfFolder(folder.first().folderId)
                                            toDownload = packs.size
                                            packs.forEach {
                                                downloadPack(it.id, folder.first().folderId)
                                            }
                                        }
                                    }
                                }, {
                                    handleException(it)
                                })
                    }, {
                        handleException(it)
                    })
                }, {
                    handleException(it)
                })
            }
            TYPE_CARD -> {
                toDownload = 1
                downloadPack(id)
            }
            else -> {
                throw Error("Wrong type")
            }
        }
    }

    private fun downloadPack(id: Long, inFolder: Long? = null) {
        val packRef = packInteractor.getPack(id).subscribe({ packDto ->
            isOwn = packDto.isOwn
            val getCntDisposable =
                cardInteractor.getWordCardCntByPack(id).subscribe({ cardCnt ->
                    val cardFilterDisposable = cardInteractor.getByPackId(id, cardCnt.count, 0)
                        .subscribe({ nullableList ->
                            nullableList?.let { list ->
                                viewModelScope.launch {
                                    val packId: Long
                                    val pack = dao.getPackByOnlineId(id)
                                    if (pack.isEmpty()) {
                                        packId = dao.insertPack(
                                            packDto.toLocal().copy(
                                                category = packDto.category.lowercase(),
                                                packName = packDto.packName.lowercase(),
                                                onlineId = packDto.id
                                            )
                                        )
                                        if (inFolder != null) {
                                            dao.insertPackFolderCrossRef(
                                                PackFolderCrossRef(
                                                    inFolder,
                                                    packId
                                                )
                                            )
                                        }
                                    } else {
                                        packId = pack.first().packId
                                        dao.deleteWordCards(packId)
                                        if (inFolder != null) {
                                            dao.insertPackFolderCrossRef(
                                                PackFolderCrossRef(
                                                    inFolder,
                                                    packId
                                                )
                                            )
                                        }
                                    }
                                    list.forEach { card ->
                                        dao.insertWordCard(
                                            card.toLocal().copy(inPackId = packId)
                                        )
                                    }
                                    if (--toDownload == 0) {
                                        downloaded.value = Unit
                                    }
                                }
                            }
                        }, {
                            handleException(it)
                        })
                }, {
                    handleException(it)
                })
        }, {
            handleException(it)
        })
    }

    private fun loadPage(pageNumber: Int, loadTo: MutableLiveData<List<SearchableModelBase>>) {
        val offset = pageNumber * PAGE_SIZE
        when (type) {
            TYPE_SINGLE -> {
                val disposable = packInteractor.getPacksByFolder(
                    id,
                    PAGE_SIZE,
                    offset
                ).subscribe({ nullableList ->
                    nullableList?.let { list ->
                        val response = ArrayList<SearchableModelSingle>()

                        list.forEach { model ->
                            response.add(SearchableModelSingle(model.toLocal()))
                        }

                        loadTo.value = response
                    }
                }, {
                    handleException(it)
                })
            }
            TYPE_CARD -> {
                val disposable = cardInteractor.getByPackId(
                    id,
                    PAGE_SIZE,
                    offset
                ).subscribe({ nullableList ->
                    nullableList?.let { list ->
                        val response = ArrayList<SearchableModelCard>()

                        list.forEach { model ->
                            response.add(SearchableModelCard(model.toLocal()))
                        }

                        loadTo.value = response
                    }
                }, {
                    handleException(it)
                })
            }
        }
    }

    fun loadHeader() {
        when (type) {
            TYPE_SINGLE -> {
                val folderRef = folderInteractor.getFolder(id).subscribe({ folderDto ->
                    isOwn = folderDto.isOwn ?: false
                    val userRef =
                        userInteractor.getUser(folderDto.userId).subscribe({ userModel ->
                            name.value = folderDto.folderName
                            upVotes.value = folderDto.upVotes
                            downVotes.value = folderDto.downVotes
                            user.value = userModel.toLocal()
                        }, {
                            handleException(it)
                        })
                }, {
                    handleException(it)
                })
            }
            TYPE_CARD -> {
                val packRef = packInteractor.getPack(id).subscribe({ packDto ->
                    isOwn = packDto.isOwn
                    val userRef =
                        userInteractor.getUser(packDto.userId).subscribe({ userModel ->
                            name.value = packDto.packName
                            upVotes.value = packDto.upVotes
                            downVotes.value = packDto.downVotes
                            user.value = userModel.toLocal()
                        }, {
                            handleException(it)
                        })
                }, {
                    handleException(it)
                })
            }
            else -> {
                throw Error("Wrong type")
            }
        }
    }

    fun getVotes() {
        if (type == TYPE_SINGLE) {
            val folderDisposable = voteInteractor.getVotesByFolder(id).subscribe({
                upVotes.value = it.upVotes
                downVotes.value = it.downVotes
            }, {
                handleException(it)
            })
        } else {
            val packDisposable = voteInteractor.getVotesByPack(id).subscribe({
                upVotes.value = it.upVotes
                downVotes.value = it.downVotes
            }, {
                handleException(it)
            })
        }
    }

    fun getOwnVote() {
        if (type == TYPE_SINGLE) {
            val folderDisposable = voteInteractor.getVoteByFolder(id).subscribe({
                ownVote.value = it
            }, {
                if (it.message?.contains("End") == true) {
                    ownVote.value = null
                } else {
                    handleException(it)
                }
            })
        } else {
            val packDisposable = voteInteractor.getVoteByPack(id).subscribe({
                ownVote.value = it
            }, {
                if (it.message?.contains("End") == true) {
                    ownVote.value = null
                } else {
                    handleException(it)
                }
            })
        }
    }

    fun vote(isUpVoted: Boolean, isDownVoted: Boolean) {
        var voteModel: VoteModel
        if (type == TYPE_SINGLE) {
            voteModel = VoteModel(
                id = -1,
                fromUser = -1,
                toUser = user.value?.userId ?: -1,
                isUpvote = isUpVoted,
                onFolder = id,
                onPack = null
            )
        } else {
            voteModel = VoteModel(
                id = -1,
                fromUser = -1,
                toUser = user.value?.userId ?: -1,
                isUpvote = isUpVoted,
                onFolder = null,
                onPack = id
            )
        }
        if (isUpVoted || isDownVoted) {
            val voteDisposable = voteInteractor.addVote(
                voteModel
            ).subscribe({
                updateVotes()
            }, {
                handleException(it)
            })
        } else {
            val deleteDisposable = voteInteractor.deleteVote(
                voteModel
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

}
