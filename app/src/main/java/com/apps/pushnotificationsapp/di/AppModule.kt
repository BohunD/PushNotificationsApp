package com.apps.pushnotificationsapp.di

import android.content.Context
import androidx.room.Room
import com.apps.pushnotificationsapp.data.db.ReminderDatabase
import com.apps.pushnotificationsapp.data.db.ReminderDao
import com.apps.pushnotificationsapp.data.repository.ReminderRepositoryImpl
import com.apps.pushnotificationsapp.domain.repository.ReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideReminderDatabase(@ApplicationContext appContext: Context): ReminderDatabase {
        return Room.databaseBuilder(
            appContext,
            ReminderDatabase::class.java,
            "reminder_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReminderDao(reminderDatabase: ReminderDatabase): ReminderDao {
        return reminderDatabase.reminderDao()
    }

    @Provides
    @Singleton
    fun provideReminderRepository(reminderDao: ReminderDao): ReminderRepository {
        return ReminderRepositoryImpl(reminderDao)
    }
}
