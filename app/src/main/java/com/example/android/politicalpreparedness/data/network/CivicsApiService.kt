package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.network.jsonadapter.DateAdapter
import com.example.android.politicalpreparedness.data.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.data.network.models.ElectionResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// TODO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
	.add(ElectionAdapter())
	.add(DateAdapter())
	.add(KotlinJsonAdapterFactory())
	.build()

private val retrofit = Retrofit.Builder()
	.addConverterFactory(MoshiConverterFactory.create(moshi))
	.client(CivicsHttpClient.getClient())
	.baseUrl(BASE_URL)
	.build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
	//TODO: Add elections API Call /elections
	@GET("elections")
	suspend fun getElections(): ElectionResponse

	//TODO: Add voterinfo API Call /voterinfo
	@GET("voterinfo")
	suspend fun getVoterInfo(): VoterInfoResponse

	//TODO: Add representatives API Call /representatives
	@GET("representatives")
	suspend fun getRepresentatives(): VoterInfoResponse
}

object CivicsApi {
	val retrofitService: CivicsApiService by lazy {
		retrofit.create(CivicsApiService::class.java)
	}
}