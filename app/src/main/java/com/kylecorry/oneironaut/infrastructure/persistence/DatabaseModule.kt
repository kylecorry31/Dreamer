package com.kylecorry.oneironaut.infrastructure.persistence

import android.content.Context
import com.kylecorry.oneironaut.app.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideDreamDao(database: AppDatabase): DreamDao {
        return database.dreamDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }
}