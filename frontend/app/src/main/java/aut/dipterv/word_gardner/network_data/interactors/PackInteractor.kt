package aut.dipterv.word_gardner.network_data.interactors

import aut.dipterv.word_gardner.network_data.apis.PackApi
import aut.dipterv.word_gardner.network_data.models.CardModel
import aut.dipterv.word_gardner.network_data.models.PackModel
import aut.dipterv.word_gardner.network_data.models.dtos.CountDto
import aut.dipterv.word_gardner.network_data.models.dtos.PackDto
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PackInteractor(private val packApi: PackApi) {

    fun getAllPacks(): Single<List<PackModel>> {
        return packApi.getAllPacks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addPack(pack: PackModel): Single<Long> {
        return packApi.addPack(pack)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updatePack(pack: PackModel): Single<Long> {
        return packApi.updatePack(pack)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPack(packId: Long): Single<PackDto> {
        return packApi.getPack(packId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deletePack(packId: Long): Single<Boolean> {
        return packApi.deletePack(packId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPacksByFilter(filter: SearchFilter, limit: Int, offset: Int): Single<List<PackModel>> {
        return packApi.getPacksByFilter(filter, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPackCnt(filter: SearchFilter): Single<CountDto> {
        return packApi.getPackCnt(filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPackCntByFolder(folderId: Long): Single<CountDto> {
        return packApi.getPackCntByFolder(folderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPacksByFolder(folderId: Long, limit: Int, offset: Int): Single<List<PackModel>> {
        return packApi.getPacksByFolder(folderId, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addWordCard(model: CardModel): Single<Long> {
        return packApi.addWordCard(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
