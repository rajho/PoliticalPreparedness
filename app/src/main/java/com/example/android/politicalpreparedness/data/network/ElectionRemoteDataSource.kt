package com.example.android.politicalpreparedness.data.network

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRemoteDataSource @Inject constructor(
	private val civicsApiService: CivicsApiService
): IElectionDataSource{
	override suspend fun getRemoteElections(): List<Election> {
		return civicsApiService.getElections().elections
	}

	override suspend fun getVoterInfo(electionId: Int, address: String): VoterInfoResponse {
		return civicsApiService.getVoterInfo(electionId, address)
	}

	override suspend fun getElectionById(electionId: Int): Election? {
		throw UnsupportedOperationException("Operation not supported")
	}

	override suspend fun saveElection(election: Election) {
		throw UnsupportedOperationException("Operation not supported")
	}

	override suspend fun deleteSavedElection(election: Election) {
		throw UnsupportedOperationException("Operation not supported")
	}

	override fun observeSavedElections(): LiveData<List<Election>> {
		throw UnsupportedOperationException("Operation not supported")
	}
}