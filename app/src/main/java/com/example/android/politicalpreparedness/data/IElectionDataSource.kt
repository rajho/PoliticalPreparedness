package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse

interface IElectionDataSource {

	suspend fun getRemoteElections(): List<Election>

	suspend fun getVoterInfo(electionId: Int, address: String): VoterInfoResponse

	suspend fun getElectionById(electionId: Int): Election?

	suspend fun saveElection(election: Election)

	suspend fun deleteSavedElection(election: Election)

	fun observeSavedElections(): LiveData<List<Election>>
}