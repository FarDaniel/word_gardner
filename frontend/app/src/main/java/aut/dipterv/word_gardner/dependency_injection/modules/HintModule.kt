package aut.dipterv.word_gardner.dependency_injection.modules

import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.InputToHintManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object HintModule {

    @Provides
    fun provideInputToHintManager() = InputToHintManager()

}