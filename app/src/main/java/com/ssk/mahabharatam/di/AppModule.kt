package com.ssk.mahabharatam.di

import android.app.Activity
import android.content.Context
import androidx.annotation.UiContext
import com.ssk.mahabharatam.data.repository.settings.SettingsRepository
import com.ssk.mahabharatam.data.repository.source.BookFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideBookFactory(activity: Activity): BookFactory {
        return BookFactory(activity)
    }

    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepository(context)
    }

}