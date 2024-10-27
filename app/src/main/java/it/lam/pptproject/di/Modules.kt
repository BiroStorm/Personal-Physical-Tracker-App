package it.lam.pptproject.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.model.room.StatisticsDao
import it.lam.pptproject.repository.ChartsRepository
import it.lam.pptproject.repository.ChartsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChartsRepositoryModule {

    @Singleton
    @Provides
    fun provideChartsRepository(statisticsDao: StatisticsDao): ChartsRepository =
        ChartsRepositoryImpl(statisticsDao)

    @Provides
    fun provideStatisticsDao(appDatabase: AppDatabase): StatisticsDao = appDatabase.statisticsDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }


}