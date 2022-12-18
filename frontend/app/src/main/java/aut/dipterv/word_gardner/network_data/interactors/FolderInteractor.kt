package aut.dipterv.word_gardner.network_data.interactors

import aut.dipterv.word_gardner.network_data.apis.FolderApi
import aut.dipterv.word_gardner.network_data.models.FolderModel
import aut.dipterv.word_gardner.network_data.models.PackFolderCrossRefModel
import aut.dipterv.word_gardner.network_data.models.dtos.CountDto
import aut.dipterv.word_gardner.network_data.models.dtos.FolderDto
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FolderInteractor(private val folderApi: FolderApi) {

    fun getAllFolders(): Single<List<FolderModel>> {
        return folderApi.getAllFolders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addFolder(folder: FolderModel): Single<Long> {
        return folderApi.addFolder(folder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateFolder(folder: FolderModel) {
        folderApi.updateFolder(folder)
    }

    fun getFolder(folderId: Long): Single<FolderDto> {
        return folderApi.getFolder(folderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteFolder(folderId: Long): Single<Boolean> {
        return folderApi.deleteFolder(folderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFoldersByFilter(
        filter: SearchFilter,
        limit: Int,
        offset: Int
    ): Single<List<FolderModel>> {
        return folderApi.getFoldersByFilter(filter, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFolderCnt(filter: SearchFilter): Single<CountDto> {
        return folderApi.getFolderCnt(filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addPackFolderCrossRef(packFolderCrossRef: PackFolderCrossRefModel): Single<Long> {
        return folderApi.addPackFolderCrossRef(packFolderCrossRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
