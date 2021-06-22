package com.example.android.politicalpreparedness.data.database

import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionLocalDataSource @Inject constructor(private val electionDao: ElectionDao)
	: IElectionDataSource {
	override suspend fun getAllElections(): List<Election> {
		TODO("Not yet implemented")
	}

	override suspend fun getFollowedElections(): List<Election> {
		TODO("Not yet implemented")
	}
}