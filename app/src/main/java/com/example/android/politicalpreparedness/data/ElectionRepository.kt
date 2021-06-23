package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.di.LocalDataSource
import com.example.android.politicalpreparedness.di.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(
	@LocalDataSource private val electionLocalDataSource: IElectionDataSource,
	@RemoteDataSource private val electionRemoteDataSource: IElectionDataSource,
) : IElectionDataSource {
	override suspend fun getAllElections(): List<Election> {
		return electionRemoteDataSource.getAllElections()
	}

	override fun observeSavedElections(): LiveData<List<Election>> {
		return electionLocalDataSource.observeSavedElections()
	}
}