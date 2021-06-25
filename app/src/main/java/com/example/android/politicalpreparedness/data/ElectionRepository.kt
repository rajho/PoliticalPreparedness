package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.di.LocalDataSource
import com.example.android.politicalpreparedness.di.RemoteDataSource
import com.example.android.politicalpreparedness.ui.representative.model.Representative
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(
	@LocalDataSource private val electionLocalDataSource: IElectionDataSource,
	@RemoteDataSource private val electionRemoteDataSource: IElectionDataSource,
) : IElectionDataSource {

	override suspend fun getRepresentatives(address: String): List<Representative> {
		return electionRemoteDataSource.getRepresentatives(address)
	}

	override suspend fun getRemoteElections(): List<Election> {
		return electionRemoteDataSource.getRemoteElections()
	}

	override suspend fun getVoterInfo(electionId: Int, address: String): VoterInfoResponse {
		return electionRemoteDataSource.getVoterInfo(electionId, address)
	}

	override suspend fun getElectionById(electionId: Int): Election? {
		return electionLocalDataSource.getElectionById(electionId)
	}

	override suspend fun saveElection(election: Election) {
		electionLocalDataSource.saveElection(election)
	}

	override suspend fun deleteSavedElection(election: Election) {
		electionLocalDataSource.deleteSavedElection(election)
	}

	override fun observeSavedElections(): LiveData<List<Election>> {
		return electionLocalDataSource.observeSavedElections()
	}


}