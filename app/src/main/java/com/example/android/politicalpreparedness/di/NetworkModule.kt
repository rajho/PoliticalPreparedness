package com.example.android.politicalpreparedness.di

import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

	@Singleton
	@Provides
	fun provideCivisApiService(): CivicsApiService {
		return CivicsApi.retrofitService
	}
}