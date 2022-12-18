package aut.dipterv.word_gardner.ui.practice.screens.practice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.dipterv.word_gardner.analitycs.TestIdentifier
import aut.dipterv.word_gardner.analitycs.WordStatisticsManager
import aut.dipterv.word_gardner.interfaces.PopupReadyViewModel
import aut.dipterv.word_gardner.local_data.dao.WordPackDao
import aut.dipterv.word_gardner.network_data.interactors.StatisticsInteractor
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.TipModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val dao: WordPackDao,
    private val wordStatisticsManager: WordStatisticsManager,
    private val statisticsInteractor: StatisticsInteractor
) : ViewModel(),
    PopupReadyViewModel {
    override var questionText =
        "A választott feladat komponensek nem kombinálkatóak hasznos gyakorlattá.\n" +
                "Szeretnél visszalépni?"
    override var isPopupNeeded = MutableLiveData(false)
    val cards = MutableLiveData<List<WordCardModel>>()
    val gratulationsText = MutableLiveData<String>()
    val fallback = MutableLiveData<Unit>()
    val reset = MutableLiveData<Unit>()
    val setupRecommendations = MutableLiveData<HashMap<Int, Float>>()
    var questionNumber = 0
    var goodAnswers = 0
    var wrongAnswers = 0

    fun loadCards(ids: ArrayList<Long>) {
        viewModelScope.launch {
            val newCards = ArrayList<WordCardModel>()
            ids.forEach { id ->
                val wordCards = dao.getWordCardsOfPack(id)
                wordCards.first().wordCards.forEach {
                    newCards.add(
                        WordCardModel(
                            foreignWord = it.foreignWord,
                            nativeWord = it.nativeWord,
                            backGround = it.backGround
                        )
                    )
                }
            }
            newCards.shuffle()
            cards.value = newCards
        }
    }

    fun setupRecommendations(hints: List<HintType>, inputs: List<InputType>) {
        val statisticsDisposable =
            statisticsInteractor.getStatistics().subscribe({ statistics ->
                setupRecommendations.value =
                    wordStatisticsManager.getRecommendationsBackend(hints, inputs, statistics)
            }, {
                viewModelScope.launch {
                    setupRecommendations.value =
                        wordStatisticsManager.getRecommendationsFrontend(hints, inputs)
                }
            })
    }

    fun testStarted(questionNumber: Int) {
        wordStatisticsManager.startTimer()
        this.questionNumber = questionNumber
        goodAnswers = 0
        wrongAnswers = 0
    }

    fun processSolution(tipModel: TipModel, testIdentifier: TestIdentifier) {
        viewModelScope.launch {
            if (tipModel.isCorrect) {
                goodAnswers += 1
            } else {
                wrongAnswers += 1
            }

            if ((goodAnswers + wrongAnswers) >= questionNumber) {
                gratulationsText.value =
                    if (wrongAnswers == 0 || (goodAnswers.toLong() / wrongAnswers) >= 1) {
                        "Szép munka!\n$goodAnswers/${(goodAnswers + wrongAnswers)}"
                    } else {
                        "Legközelebb már jobb lesz 😉\nhelyes válaszok: $goodAnswers"
                    }
            }
            val rawWordStatistics = wordStatisticsManager.getRawStatistics(
                tipModel.word.foreignWord,
                testIdentifier,
                tipModel.isCorrect
            )

            rawWordStatistics?.let { rawWordStatistics ->
                val statisticsDisposable =
                    statisticsInteractor.addRawWordStatistics(rawWordStatistics).subscribe({}, {})
            }

        }
    }

    override fun accepted() {
        isPopupNeeded.value = false
        fallback.value = Unit
    }

    override fun declined() {
        reset.value = Unit
    }

}

