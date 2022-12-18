package aut.dipterv.word_gardner.network_data.interactors

import android.util.Log
import aut.dipterv.word_gardner.network_data.apis.VoteApi
import aut.dipterv.word_gardner.network_data.models.VoteModel
import aut.dipterv.word_gardner.network_data.models.dtos.VotesDto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VoteInteractor(private val voteApi: VoteApi) {


    fun addVote(vote: VoteModel): Single<Long> {
        Log.d("MAKING", "adding")
        return voteApi.addVote(vote)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getVoteByUser(userId: Long): Single<VoteModel?> {
        return voteApi.getVoteByUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getVoteByPack(packId: Long): Single<VoteModel?> {
        return voteApi.getVoteByPack(packId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getVoteByFolder(folderId: Long): Single<VoteModel?> {
        return voteApi.getVoteByFolder(folderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getVotesByUser(userId: Long): Single<VotesDto> {
        return voteApi.getVotesByUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getVotesByPack(packId: Long): Single<VotesDto> {
        return voteApi.getVotesByPack(packId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getVotesByFolder(folderId: Long): Single<VotesDto> {
        return voteApi.getVotesByFolder(folderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun deleteVote(vote: VoteModel): Single<Unit> {
        Log.d("MAKING", "deleting")
        return voteApi.deleteVote(vote)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}