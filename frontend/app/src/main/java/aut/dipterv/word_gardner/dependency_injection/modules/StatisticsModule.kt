package aut.dipterv.word_gardner.dependency_injection.modules

import aut.dipterv.word_gardner.analitycs.WordStatisticsManager
import aut.dipterv.word_gardner.local_data.dao.WordPackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {

    @Provides
    @Singleton
    fun provideWordStatisticsManager(dao: WordPackDao) = WordStatisticsManager(dao)
}
