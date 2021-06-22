package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRemoteDataSource @Inject constructor(
	private val civicsApiService: CivicsApiService
): IElectionDataSource{
	override suspend fun getAllElections(): List<Election> {
		return civicsApiService.getElections().elections
	}

	override suspend fun getFollowedElections(): List<Election> {
		throw UnsupportedOperationException("Operation not supported")
	}
}