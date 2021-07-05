package com.example.android.politicalpreparedness.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.data.network.models.Election

@Dao
interface ElectionDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun save(election: Election)

	@Query("SELECT * FROM election_table")
	fun getAll(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
	fun getById(id: Int): Election?

    @Delete
    suspend fun delete(vararg elections: Election)

    @Query("DELETE FROM election_table")
    suspend fun clear()
}