package aut.dipterv.word_gardner.network_data.apis

import aut.dipterv.word_gardner.network_data.models.CardModel
import aut.dipterv.word_gardner.network_data.models.PackModel
import aut.dipterv.word_gardner.network_data.models.dtos.CountDto
import aut.dipterv.word_gardner.network_data.models.dtos.PackDto
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import io.reactivex.Single
import retrofit2.http.*

interface PackApi {

    companion object {
        private const val BASE_PATH = "data"
        const val CARDS = "$BASE_PATH/card"
        const val PACKS = "$BASE_PATH/pack"
        const val PACK = "$PACKS/{id}"
        const val FILTERED_PACKS = "$PACKS/filtered/{limit}/{offset}"
        const val BY_USER = "$PACKS/user/{id}"
        const val COUNT = "$PACKS/count"
        const val COUNT_BY_FOLDER = "$COUNT/{folder_id}"
        const val BY_FOLDER = "$PACKS/{folder_id}/{limit}/{offset}"
    }

    @GET(PACKS)
    fun getAllPacks(): Single<List<PackModel>>

    @POST(PACKS)
    fun addPack(
        @Body pack: PackModel
    ): Single<Long>

    @PUT(PACKS)
    fun updatePack(
        @Body pack: PackModel
    ): Single<Long>

    @GET(PACK)
    fun getPack(
        @Path("id") packId: Long
    ): Single<PackDto>


    @DELETE(PACK)
    fun deletePack(
        @Path("id") packId: Long
    ): Single<Boolean>

    @PATCH(FILTERED_PACKS)
    fun getPacksByFilter(
        @Body filter: SearchFilter,
        @Path("limit") limit: Int,
        @Path("offset") offset: Int
    ): Single<List<PackModel>>

    @GET(BY_USER)
    fun getPacksByUser(
        @Path("id") userId: String
    ): Single<List<PackModel>>

    @PATCH(COUNT)
    fun getPackCnt(
        @Body filter: SearchFilter
    ): Single<CountDto>

    @GET(COUNT_BY_FOLDER)
    fun getPackCntByFolder(
        @Path(
            "folder_id"
        ) folderId: Long
    ): Single<CountDto>

    @GET(BY_FOLDER)
    fun getPacksByFolder(
        @Path("folder_id") folderId: Long,
        @Path("limit") limit: Int,
        @Path("offset") offset: Int
    ): Single<List<PackModel>>

    @POST(CARDS)
    fun addWordCard(
        @Body model: CardModel
    ): Single<Long>

}
