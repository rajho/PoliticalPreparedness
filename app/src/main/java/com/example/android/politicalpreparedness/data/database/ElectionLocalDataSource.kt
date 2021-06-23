package com.example.android.politicalpreparedness.data.database

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionLocalDataSource @Inject constructor(
	private val electionDao: ElectionDao,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : IElectionDataSource {

	override suspend fun getElectionById(electionId: Int): Election? = withContext(ioDispatcher) {
		return@withContext electionDao.getById(electionId)
	}

	override suspend fun saveElection(election: Election) = withContext(ioDispatcher) {
		return@withContext electionDao.save(election)
	}

	override suspend fun deleteSavedElection(election: Election) = withContext(ioDispatcher){
		return@withContext electionDao.delete(election)
	}

	override fun observeSavedElections(): LiveData<List<Election>> {
		return electionDao.getAll()
	}

	override suspend fun getRemoteElections(): List<Election> {
		throw UnsupportedOperationException("Operation not supported")
	}

	override suspend fun getVoterInfo(electionId: Int, address: String): VoterInfoResponse {
		throw UnsupportedOperationException("Operation not supported")
	}
}