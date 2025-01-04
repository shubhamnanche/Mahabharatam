package com.ssk.mahabharatam.di

import android.app.Activity
import com.ssk.mahabharatam.source.BookFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideBookFactory(activity: Activity): BookFactory {
        return BookFactory(activity)
    }
}