package it.lam.pptproject.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.lam.pptproject.repository.TrackingRepository

/*
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    fun provideTrackingRepository(context: Context): TrackingRepository {
        return TrackingRepository(context) // Passa il contesto al TrackingRepository
    }
}

 */