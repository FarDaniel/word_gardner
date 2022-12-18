package aut.dipterv.word_gardner.ui.browsing.screens.type_search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aut.dipterv.word_gardner.network_data.interactors.FolderInteractor
import aut.dipterv.word_gardner.network_data.interactors.PackInteractor
import aut.dipterv.word_gardner.network_data.interactors.UserInteractor
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase.SearchableType.*
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelMultiple
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelSingle
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelUser
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class TypeSearchViewModel @Inject constructor(
    private val packInteractor: PackInteractor,
    private val folderInteractor: FolderInteractor,
    private val userInteractor: UserInteractor
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 8
    }

    val previousModels = MutableLiveData<List<SearchableModelBase>>()
    val nextModels = MutableLiveData<List<SearchableModelBase>>()
    val toJumpMainModels = MutableLiveData<List<SearchableModelBase>>()
    val initialModels = MutableLiveData<List<SearchableModelBase>>()
    val actualPage = MutableLiveData(0)
    val allPage = MutableLiveData(0)

    val successfulCall = MutableLiveData<Unit>()
    val fault = MutableLiveData<Unit>()
    val login = MutableLiveData<Unit>()

    lateinit var type: SearchableModelBase.SearchableType
    lateinit var filter: SearchFilter

    fun init() {
        when (type) {
            TYPE_SINGLE -> {
                val getCntDisposable = packInteractor.getPackCnt(filter).subscribe({
                    allPage.value = ceil((it.count).toDouble() / PAGE_SIZE).toInt()
                    if ((allPage.value ?: 0) > PAGE_SIZE * 2) {
                        val packByFilterDisposable = packInteractor.getPacksByFilter(
                            filter, PAGE_SIZE * 2, (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelSingle>()
                                val lastPageDisposable = packInteractor.getPacksByFilter(
                                    filter, PAGE_SIZE, (((allPage.value ?: 1) - 1)) * PAGE_SIZE
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
                        val packByFilterDisposable = packInteractor.getPacksByFilter(
                            filter, PAGE_SIZE * 2, (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
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
            TYPE_MULTIPLE -> {
                val getCntDisposable = folderInteractor.getFolderCnt(filter).subscribe({
                    allPage.value = ceil((it.count).toDouble() / PAGE_SIZE).toInt()
                    if ((allPage.value ?: 0) > PAGE_SIZE * 2) {
                        val folderFilterDisposable = folderInteractor.getFoldersByFilter(
                            filter, PAGE_SIZE * 2, (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelMultiple>()
                                val lastPageDisposable = folderInteractor.getFoldersByFilter(
                                    filter, PAGE_SIZE, (allPage.value ?: 0) - 1
                                ).subscribe({ nullableLPList ->
                                    successfulCall.value = Unit
                                    nullableLPList?.let { lPList ->
                                        lPList.forEach { model ->
                                            response.add(SearchableModelMultiple(model.toLocal()))
                                        }
                                    }
                                    list.forEach { model ->
                                        response.add(SearchableModelMultiple(model.toLocal()))
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
                        val packByFilterDisposable = folderInteractor.getFoldersByFilter(
                            filter, PAGE_SIZE * 2, (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            successfulCall.value = Unit
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelMultiple>()

                                list.forEach { model ->
                                    response.add(SearchableModelMultiple(model.toLocal()))
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
            TYPE_USER -> {
                val getCntDisposable = userInteractor.getUserCnt(filter).subscribe({
                    allPage.value = ceil((it.count).toDouble() / PAGE_SIZE).toInt()
                    if ((allPage.value ?: 0) > PAGE_SIZE * 2) {
                        val packByFilterDisposable = userInteractor.getUsersByFilter(
                            filter, PAGE_SIZE * 2, (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            successfulCall.value = Unit
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelUser>()
                                val lastPageDisposable = userInteractor.getUsersByFilter(
                                    filter, PAGE_SIZE, ((allPage.value ?: 0) - 1) * PAGE_SIZE
                                ).subscribe({ nullableLPList ->
                                    nullableLPList?.let { lPList ->
                                        lPList.forEach { model ->
                                            response.add(SearchableModelUser(model.toLocal()))
                                        }
                                    }
                                    list.forEach { model ->
                                        response.add(SearchableModelUser(model.toLocal()))
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
                        val packByFilterDisposable = userInteractor.getUsersByFilter(
                            filter, PAGE_SIZE * 2, (actualPage.value ?: 0) * PAGE_SIZE
                        ).subscribe({ nullableList ->
                            successfulCall.value = Unit
                            nullableList?.let { list ->
                                val response = ArrayList<SearchableModelUser>()

                                list.forEach { model ->
                                    response.add(SearchableModelUser(model.toLocal()))
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
            }, previousModels
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
            }, nextModels
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
            }, previousModels
        )
        loadPage(
            if (page == (allPage.value ?: 0) - 1) {
                0
            } else {
                page + 1
            }, nextModels
        )
    }

    private fun loadPage(pageNumber: Int, loadTo: MutableLiveData<List<SearchableModelBase>>) {
        val offset = pageNumber * PAGE_SIZE
        when (type) {
            TYPE_SINGLE -> {
                val disposable = packInteractor.getPacksByFilter(
                    filter, PAGE_SIZE, offset
                ).subscribe({ nullableList ->
                    successfulCall.value = Unit
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
            TYPE_MULTIPLE -> {
                val disposable = folderInteractor.getFoldersByFilter(
                    filter, PAGE_SIZE, offset
                ).subscribe({ nullableList ->
                    successfulCall.value = Unit
                    nullableList?.let { list ->
                        val response = ArrayList<SearchableModelMultiple>()

                        list.forEach { model ->
                            response.add(SearchableModelMultiple(model.toLocal()))
                        }

                        loadTo.value = response
                    }
                }, {
                    handleException(it)
                })
            }
            TYPE_USER -> {
                val disposable = userInteractor.getUsersByFilter(
                    filter, PAGE_SIZE, offset
                ).subscribe({ nullableList ->
                    successfulCall.value = Unit
                    nullableList?.let { list ->
                        val response = ArrayList<SearchableModelUser>()

                        list.forEach { model ->
                            response.add(SearchableModelUser(model.toLocal()))
                        }

                        loadTo.value = response
                    }
                }, {
                    handleException(it)
                })
            }
        }
    }

    private fun handleException(exception: Throwable) {
        try {
            exception.printStackTrace()
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
