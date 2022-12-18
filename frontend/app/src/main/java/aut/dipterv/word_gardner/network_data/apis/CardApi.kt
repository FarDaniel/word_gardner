package aut.dipterv.word_gardner.network_data.apis

import  aut.dipterv.word_gardner.network_data.models.CardModel
import aut.dipterv.word_gardner.network_data.models.dtos.CountDto
import io.reactivex.Single
import retrofit2.http.*

interface CardApi {

    companion object {
        private const val BASE_PATH = "data"
        const val CARDS = "$BASE_PATH/card"
        const val CARD = "$CARDS/{id}"
        const val COUNT = "$CARDS/count/{pack_id}"
        const val PAGING = "$CARDS/{pack_id}/{limit}/{offset}"
    }

    @POST(CARDS)
    fun addCard(card: CardModel): Single<String>

    @PUT(CARDS)
    fun updateCard(
        @Body() card: CardModel
    )

    @GET(CARD)
    fun getByUserId(
        @Path("id") cardId: String
    ): Single<CardModel>


    @DELETE(CARD)
    fun deleteCard(
        @Path("id") cardId: String
    )

    @GET(COUNT)
    fun getCntByPack(
        @Path("pack_id") packId: Long
    ): Single<CountDto>

    @GET(PAGING)
    fun getByPack(
        @Path("pack_id") packId: Long,
        @Path("limit") limit: Int,
        @Path("offset") offset: Int
    ): Single<List<CardModel>>

}
