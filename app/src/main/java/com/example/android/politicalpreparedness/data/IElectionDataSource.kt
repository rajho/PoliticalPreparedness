package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Election

interface IElectionDataSource {

	suspend fun getAllElections(): List<Election>

	fun observeSavedElections(): LiveData<List<Election>>
}