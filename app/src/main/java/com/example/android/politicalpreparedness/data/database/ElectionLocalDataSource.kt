package com.example.android.politicalpreparedness.data.database

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import java.lang.UnsupportedOperationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionLocalDataSource @Inject constructor(private val electionDao: ElectionDao)
	: IElectionDataSource {
	override suspend fun getAllElections(): List<Election> {
		throw UnsupportedOperationException("NO-OP")
	}

	override fun observeSavedElections(): LiveData<List<Election>> {
		return electionDao.getAll()
	}
}