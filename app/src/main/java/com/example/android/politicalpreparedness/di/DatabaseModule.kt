package com.example.android.politicalpreparedness.di

import android.content.Context
import androidx.room.Room
import com.example.android.politicalpreparedness.data.database.ElectionDao
import com.example.android.politicalpreparedness.data.database.ElectionDatabase
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
	@Singleton
	fun provideDatabase(@ApplicationContext appContext: Context): ElectionDatabase {
		return ElectionDatabase.getInstance(appContext)
	}

	@Provides
	fun provideElectionDao(database: ElectionDatabase): ElectionDao {
		return database.electionDao
	}
}