package aut.dipterv.word_gardner.dependency_injection.modules

import android.content.Context
import aut.dipterv.word_gardner.local_data.database.WordPackDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context) =
        WordPackDatabase.getInstance(context).wordPackDao()

}
