package aut.dipterv.word_gardner.ui.practice.screens.edit_pack

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.dipterv.word_gardner.interfaces.PopupReadyViewModel
import aut.dipterv.word_gardner.local_data.dao.WordPackDao
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty
import aut.dipterv.word_gardner.local_data.models.Pack
import aut.dipterv.word_gardner.local_data.models.WordCard
import aut.dipterv.word_gardner.network_data.interactors.PackInteractor
import aut.dipterv.word_gardner.network_data.interceptors.SessionManager
import aut.dipterv.word_gardner.network_data.models.CardModel
import aut.dipterv.word_gardner.network_data.models.PackModel
import aut.dipterv.word_gardner.ui.practice.screens.edit_pack.new_word_recycler_view.NewWordModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EditPackViewModel @Inject constructor(
    private val dao: WordPackDao,
    private val packInteractor: PackInteractor
) : ViewModel(),
    PopupReadyViewModel {

    override var questionText = ""
    override var isPopupNeeded: MutableLiveData<Boolean> = MutableLiveData(false)
    val categories = MutableLiveData<List<String>>()
    val diff: MutableLiveData<Difficulty> = MutableLiveData(
        Difficulty.UNDEFINED
    )
    val toPackSelector = MutableLiveData<Unit>()
    val newWord = MutableLiveData<NewWordModel>()
    val categoryText = MutableLiveData<String>()
    val packNameText = MutableLiveData<String>()
    val loginNeeded = MutableLiveData<() -> Unit>()
    var savedCardCnt = 0
    val fault = MutableLiveData<Unit>()
    val navigateForward = MutableLiveData<Unit>()
    var packId: Long = -1
    var onlineId: Long? = null
    var isPublic = true
    var saveOfflineAfterPopup = false
    private val wordCards: ArrayList<WordCard> = ArrayList()

    fun setPackFromDao(id: Long) {
        viewModelScope.launch {
            if (id > 0) {
                val wordCardsOfPack = dao.getWordCardsOfPack(id)
                packId = id
                categoryText.value = wordCardsOfPack.first().pack.category
                packNameText.value = wordCardsOfPack.first().pack.packName

                onlineId = wordCardsOfPack.first().pack.onlineId
                Log.d("MAKING", "" + onlineId)
                diff.value = wordCardsOfPack.first().pack.difficulty
                wordCards.addAll(wordCardsOfPack.first().wordCards)
                wordCards.forEach {
                    newWord.value =
                        NewWordModel(
                            nativeWord = it.nativeWord,
                            foreignWord = it.foreignWord,
                            backGround = it.backGround
                        )
                }
            }
            loadCategories()
        }
    }

    fun savePack(
        context: Context,
        categoryText: String,
        packNameText: String,
        difficulty: Difficulty?,
        cards: List<NewWordModel>
    ) {
        viewModelScope.launch {
            val packToSave =
                if (packId > -1) {
                    Pack(
                        packId = packId,
                        onlineId = onlineId,
                        category = categoryText,
                        packName = packNameText,
                        difficulty = difficulty ?: Difficulty.UNDEFINED
                    )
                } else {
                    Pack(
                        onlineId = onlineId,
                        category = categoryText,
                        packName = packNameText,
                        difficulty = difficulty ?: Difficulty.UNDEFINED
                    )
                }

            viewModelScope.launch {
                packId = dao.insertPack(packToSave)
                saveWords(cards)

                if (isPublic && onlineId == null) {
                    savePackToBackend(context, cards, packToSave)
                } else {
                    navigateForward.value = Unit
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun savePackToBackend(
        context: Context,
        cards: List<NewWordModel>,
        pack: Pack
    ) {
        val sessionManager = SessionManager(context)
        if (sessionManager.fetchIsOnline()) {
            val insertDisposable =
                packInteractor.addPack(PackModel.fromLocal(pack)).subscribe({
                    viewModelScope.launch {
                        dao.insertPack(pack.copy(packId = packId, onlineId = it))
                        onlineId = it
                        cards.forEach { newCard ->
                            val packDisposable = packInteractor.addWordCard(
                                CardModel.fromLocal(
                                    WordCard.fromNewWordModel(
                                        newCard,
                                        packId
                                    ),
                                    onlineId ?: -1
                                )
                            ).subscribe({
                                if (++savedCardCnt >= cards.size) {
                                    navigateForward.value = Unit
                                }
                            }, {
                                packInteractor.deletePack(onlineId ?: -1)
                                handleException(it) { savePackToBackend(context, cards, pack) }
                            })
                        }
                    }
                }) {
                    handleException(it) { savePackToBackend(context, cards, pack) }
                }
        }
    }

    fun popupErrorMessage(text: String, saveOffline: Boolean) {
        saveOfflineAfterPopup = saveOffline
        questionText = text
        isPopupNeeded.value = true
    }

    private fun saveWords(allWords: List<NewWordModel>) {
        viewModelScope.launch {
            dao.deleteWordCards(packId)
            allWords.forEach { item ->
                dao.insertWordCard(WordCard.fromNewWordModel(item, packId))
            }
        }
    }

    fun autoMakeNewWords(autoAddText: String, isNativeToAdd: Boolean) {
        val wordsArray = ArrayList<String>()
        autoAddText.split(Regex("([^\\p{L}])+")).forEach { word ->
            if (word.length > 1)
                wordsArray.add(word)
        }
        if (isNativeToAdd)
            wordsArray.forEach { freshWord ->
                newWord.value = NewWordModel(freshWord, "", CardColor.GRAY)
            }
        else
            wordsArray.forEach { freshWord ->
                newWord.value = NewWordModel("", freshWord, CardColor.GRAY)
            }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val list = ArrayList<String>()
            list.addAll(dao.getPackCategories())
            list.addAll(dao.getFolderCategories())
            categories.value = list.distinct()
        }
    }

    override fun accepted() {
        isPopupNeeded.value = false
        if (packId > 0 && !saveOfflineAfterPopup) {
            viewModelScope.launch {
                dao.deleteWordCards(packId)
                dao.deletePack(packId)
                if (onlineId != null) {
                    val disposable = packInteractor.deletePack(onlineId ?: -1).subscribe({}, {
                        handleException(it) { accepted() }
                    })
                }
                toPackSelector.value = Unit
            }
        } else {
            toPackSelector.value = Unit
        }
    }

    private fun handleException(exception: Throwable, afterLogin: () -> Unit) {
        try {
            if ((exception as HttpException).code() == 401) {
                loginNeeded.value = afterLogin
            } else {
                fault.value = Unit
            }
        } catch (e: java.lang.Exception) {
            fault.value = Unit
        }
    }

}
