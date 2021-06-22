package com.example.android.politicalpreparedness.di

import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.database.ElectionLocalDataSource
import com.example.android.politicalpreparedness.data.network.ElectionRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class LocalDataSource

@Qualifier
annotation class RemoteDataSource

@Qualifier
annotation class RepositoryDataSource


@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

	@LocalDataSource
	@Singleton
	@Binds
	abstract fun bindLocalDataSource(impl: ElectionLocalDataSource): IElectionDataSource

	@RemoteDataSource
	@Singleton
	@Binds
	abstract fun bindRemoteDataSource(impl: ElectionRemoteDataSource): IElectionDataSource

	@RepositoryDataSource
	@Singleton
	@Binds
	abstract fun bindRepository(impl: ElectionRepository): IElectionDataSource
}