package com.example.android.politicalpreparedness.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.data.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun save(election: Election)

    //TODO: Add select all election query
	@Query("SELECT * FROM election_table")
	fun getAll(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
	fun getById(id: Int): Election?

    //TODO: Add delete query
	@Delete
    suspend fun delete(vararg elections: Election)

    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    suspend fun clear()
}