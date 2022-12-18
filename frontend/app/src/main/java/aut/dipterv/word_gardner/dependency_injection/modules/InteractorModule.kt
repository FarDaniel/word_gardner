package aut.dipterv.word_gardner.dependency_injection.modules

import aut.dipterv.word_gardner.network_data.apis.*
import aut.dipterv.word_gardner.network_data.interactors.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorModule {

    @Provides
    @Singleton
    fun provideCardInteractor(cardApi: CardApi) = CardInteractor(cardApi)

    @Provides
    @Singleton
    fun provideFolderInteractor(folderApi: FolderApi) = FolderInteractor(folderApi)

    @Provides
    @Singleton
    fun providePackInteractor(packApi: PackApi) = PackInteractor(packApi)

    @Provides
    @Singleton
    fun provideUnsplashInteractor(unsplashApi: UnsplashApi) = UnsplashInteractor(unsplashApi)

    @Provides
    @Singleton
    fun provideUserInteractor(userApi: UserApi) = UserInteractor(userApi)

    @Provides
    @Singleton
    fun provideVoteInteractor(voteApi: VoteApi) = VoteInteractor(voteApi)

    @Provides
    @Singleton
    fun provideStatisticsInteractor(statisticsApi: StatisticsApi) =
        StatisticsInteractor(statisticsApi)

}
