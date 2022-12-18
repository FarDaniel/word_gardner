package aut.dipterv.word_gardner.network_data.apis

import aut.dipterv.word_gardner.network_data.models.FolderModel
import aut.dipterv.word_gardner.network_data.models.PackFolderCrossRefModel
import aut.dipterv.word_gardner.network_data.models.dtos.CountDto
import aut.dipterv.word_gardner.network_data.models.dtos.FolderDto
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import io.reactivex.Single
import retrofit2.http.*

interface FolderApi {

    companion object {
        private const val BASE_PATH = "data"
        const val FOLDERS = "$BASE_PATH/folder"
        const val PACK_FOLDER_CROSS_REF = "$BASE_PATH/pack_folder"
        const val FOLDER = "$FOLDERS/{id}"
        const val FILTERED_FOLDERS = "$FOLDERS/filtered/{limit}/{offset}"
        const val COUNT = "$FOLDERS/count"
    }

    @GET(FOLDERS)
    fun getAllFolders(): Single<List<FolderModel>>

    @POST(FOLDERS)
    fun addFolder(
        @Body folder: FolderModel
    ): Single<Long>

    @PUT(FOLDERS)
    fun updateFolder(
        @Body folder: FolderModel
    )

    @GET(FOLDER)
    fun getFolder(
        @Path("id") folderId: Long
    ): Single<FolderDto>


    @DELETE(FOLDER)
    fun deleteFolder(
        @Path("id") folderId: Long
    ): Single<Boolean>

    @PATCH(FILTERED_FOLDERS)
    fun getFoldersByFilter(
        @Body filter: SearchFilter,
        @Path("limit") limit: Int,
        @Path("offset") offset: Int
    ): Single<List<FolderModel>>

    @PATCH(COUNT)
    fun getFolderCnt(
        @Body filter: SearchFilter
    ): Single<CountDto>

    @POST(PACK_FOLDER_CROSS_REF)
    fun addPackFolderCrossRef(
        @Body packFolderCrossRef: PackFolderCrossRefModel
    ): Single<Long>

}
