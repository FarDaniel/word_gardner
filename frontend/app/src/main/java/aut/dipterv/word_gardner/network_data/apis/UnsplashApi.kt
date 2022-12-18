package aut.dipterv.word_gardner.network_data.apis

import aut.dipterv.word_gardner.network_data.models.UrlArray
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    companion object {
        private const val BASE_PATH = ""
        const val SEARCH_PHOTOS = "${BASE_PATH}search/photos"
    }

    @GET(SEARCH_PHOTOS)
    fun searchPicture(
        @Query("query") keyWord: String
    ): Single<UrlArray>
}
