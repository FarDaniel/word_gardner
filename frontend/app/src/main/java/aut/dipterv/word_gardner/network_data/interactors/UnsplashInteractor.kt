package aut.dipterv.word_gardner.network_data.interactors

import aut.dipterv.word_gardner.network_data.apis.UnsplashApi
import aut.dipterv.word_gardner.network_data.models.UrlArray
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UnsplashInteractor(private val unsplashApi: UnsplashApi) {

    fun getPicture(keyWord: String): Single<UrlArray> {
        return unsplashApi.searchPicture(keyWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
