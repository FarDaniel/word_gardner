package aut.dipterv.word_gardner.network_data.interceptors

import android.content.Context
import aut.dipterv.word_gardner.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class UnsplashAuthInterceptor(context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Authorization", "Client-ID ${BuildConfig.UNSPLASH_KEY}")

        val req = requestBuilder.build()
        return chain.proceed(req)
    }
}