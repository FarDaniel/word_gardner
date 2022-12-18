package aut.dipterv.word_gardner.dependency_injection.modules

import android.content.Context
import aut.dipterv.word_gardner.network_data.apis.*
import aut.dipterv.word_gardner.network_data.interceptors.AuthInterceptor
import aut.dipterv.word_gardner.network_data.interceptors.UnsplashAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val CARDS_URL = "http://word-gardner.herokuapp.com/api/"
    private const val UNSPLASH_URL = "https://api.unsplash.com/"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OkHttpClientForCards

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OkHttpClientForUnsplash

    @Provides
    fun provideGsonConverter() = GsonConverterFactory.create()

    @Provides
    fun provideCallAdapter() = RxJava2CallAdapterFactory.create()

    @Provides
    fun provideAuthInterceptor(@ApplicationContext context: Context) =
        AuthInterceptor(context)

    @Provides
    fun provideUnsplashAuthInterceptor(@ApplicationContext context: Context) =
        UnsplashAuthInterceptor(context)

    @Provides
    @Singleton
    @OkHttpClientForCards
    fun provideOkHttpClientForCards(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @OkHttpClientForUnsplash
    fun provideOkHttpClientForUnsplash(unsplashAuthInterceptor: UnsplashAuthInterceptor): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(unsplashAuthInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideCardApi(
        converter: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        @OkHttpClientForCards okHttpClient: OkHttpClient
    ): CardApi {
        return Retrofit.Builder()
            .baseUrl(CARDS_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(CardApi::class.java)
    }

    @Provides
    @Singleton
    fun providePackApi(
        converter: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        @OkHttpClientForCards okHttpClient: OkHttpClient
    ): PackApi {
        return Retrofit.Builder()
            .baseUrl(CARDS_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(PackApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFolderApi(
        converter: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        @OkHttpClientForCards okHttpClient: OkHttpClient
    ): FolderApi {
        return Retrofit.Builder()
            .baseUrl(CARDS_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(FolderApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(
        converter: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        @OkHttpClientForCards okHttpClient: OkHttpClient
    ): UserApi {
        return Retrofit.Builder()
            .baseUrl(CARDS_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVoteApi(
        converter: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        @OkHttpClientForCards okHttpClient: OkHttpClient
    ): VoteApi {
        return Retrofit.Builder()
            .baseUrl(CARDS_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(VoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStatistiicsApi(
        converter: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        @OkHttpClientForCards okHttpClient: OkHttpClient
    ): StatisticsApi {
        return Retrofit.Builder()
            .baseUrl(CARDS_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(StatisticsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitForUnsplash(
        converter: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        @OkHttpClientForUnsplash okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UNSPLASH_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
    }

}
