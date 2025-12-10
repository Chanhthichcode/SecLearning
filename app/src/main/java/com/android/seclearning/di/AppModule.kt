package com.android.seclearning.di

import android.content.Context
import com.android.seclearning.data.local.PreferencesManager
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**Database local*/
    @Singleton
    @Provides
    fun providerPreferencesManager(
        @ApplicationContext context: Context
    ) = PreferencesManager(context)

}
