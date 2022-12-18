package aut.dipterv.word_gardner.network_data

import android.content.Context
import aut.dipterv.word_gardner.network_data.apis.UnsplashApi
import aut.dipterv.word_gardner.network_data.interactors.UnsplashInteractor
import aut.dipterv.word_gardner.network_data.interceptors.UnsplashAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object UnsplashRetrofitFactory {
    private var unsplashRetrofit: Retrofit? = null
    private var unsplashInteractor: UnsplashInteractor? = null
    private const val UNSPLASH_URL = "https://api.unsplash.com/"

    fun getUnsplashInteractor(context: Context): UnsplashInteractor {
        if (unsplashInteractor == null) {
            (return if (unsplashRetrofit == null) {
                val api = createUnsplashRetrofit(context).create(UnsplashApi::class.java)
                val unsplashInteractor = UnsplashInteractor(api)
                this.unsplashInteractor = unsplashInteractor
                unsplashInteractor
            } else {
                val api =
                    unsplashRetrofit?.create(UnsplashApi::class.java) ?: createUnsplashRetrofit(
                        context
                    ).create(UnsplashApi::class.java)

                val unsplashInteractor = UnsplashInteractor(api)
                this.unsplashInteractor = unsplashInteractor
                unsplashInteractor
            })
        } else {
            return unsplashInteractor as UnsplashInteractor
        }
    }

    fun createUnsplashRetrofit(context: Context): Retrofit {
        val converter = GsonConverterFactory.create()
        val callAdapterFactory = RxJava2CallAdapterFactory.create()
        val clientBuilder = OkHttpClient()
            .newBuilder()
            .addInterceptor(UnsplashAuthInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(UNSPLASH_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(clientBuilder)
            .build()

        this.unsplashRetrofit = retrofit

        return retrofit
    }

}