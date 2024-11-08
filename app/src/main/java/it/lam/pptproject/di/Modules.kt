package it.lam.pptproject.di

import android.content.Context
import com.google.android.gms.fitness.FitnessLocal
import com.google.android.gms.fitness.LocalRecordingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.lam.pptproject.data.datastore.DataStoreImpl
import it.lam.pptproject.data.datastore.DataStoreRepository
import it.lam.pptproject.model.room.ActivityDao
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.model.room.StatisticsDao
import it.lam.pptproject.model.room.UserDao
import it.lam.pptproject.repository.ActivityRepository
import it.lam.pptproject.repository.ActivityRepositoryImpl
import it.lam.pptproject.repository.ChartsRepository
import it.lam.pptproject.repository.ChartsRepositoryImpl
import it.lam.pptproject.repository.UserRepository
import it.lam.pptproject.repository.UserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChartsRepositoryModule {

    @Provides
    fun provideStatisticsDao(appDatabase: AppDatabase): StatisticsDao = appDatabase.statisticsDao()

    @Provides
    fun provideUserDao(appDatabase: AppDatabase) : UserDao = appDatabase.userDao()

    @Provides
    fun provideActivityDao(appDatabase: AppDatabase) : ActivityDao = appDatabase.activityDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context,
    ): DataStoreRepository = DataStoreImpl(app)


    @Singleton
    @Provides
    fun provideChartsRepository(statisticsDao: StatisticsDao): ChartsRepository =
        ChartsRepositoryImpl(statisticsDao)


    @Singleton
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository = UserRepositoryImpl(userDao)

    @Singleton
    @Provides
    fun provideActivityRepository(activityDao : ActivityDao): ActivityRepository = ActivityRepositoryImpl(activityDao)

    @Singleton
    @Provides
    fun provideRecordingClient(@ApplicationContext app: Context): LocalRecordingClient =
        FitnessLocal.getLocalRecordingClient(app)



}