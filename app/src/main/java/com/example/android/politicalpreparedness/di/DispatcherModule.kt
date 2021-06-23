package com.example.android.politicalpreparedness.di

import android.content.Context
import com.example.android.politicalpreparedness.data.database.ElectionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class IoDispatcher

@Qualifier
annotation class MainDispatcher

@InstallIn(SingletonComponent::class)
@Module
object DispatcherModule {

	@IoDispatcher
	@Provides
	@Singleton
	fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

	@MainDispatcher
	@Provides
	@Singleton
	fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}