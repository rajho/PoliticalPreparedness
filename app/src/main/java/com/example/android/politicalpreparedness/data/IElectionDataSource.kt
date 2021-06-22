package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election

interface IElectionDataSource {

	suspend fun getAllElections(): List<Election>

	suspend fun getFollowedElections(): List<Election>
}