package aut.dipterv.word_gardner.network_data.interactors

import aut.dipterv.word_gardner.network_data.apis.CardApi
import aut.dipterv.word_gardner.network_data.models.CardModel
import aut.dipterv.word_gardner.network_data.models.dtos.CountDto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CardInteractor(private val cardApi: CardApi) {

    fun registerCard(card: CardModel): Single<String> {
        return cardApi.addCard(card)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateCard(card: CardModel) {
        cardApi.updateCard(card)
    }

    fun getByPackId(packId: Long, limit: Int, offset: Int): Single<List<CardModel>> {
        return cardApi.getByPack(packId, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteCard(cardId: String) {
        cardApi.deleteCard(cardId)
    }

    fun getWordCardCntByPack(packId: Long): Single<CountDto> {
        return cardApi.getCntByPack(packId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
