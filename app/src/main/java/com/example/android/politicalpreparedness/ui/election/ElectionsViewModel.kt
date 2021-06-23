package com.example.android.politicalpreparedness.ui.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.di.RepositoryDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO: Construct ViewModel and provide election datasource
@HiltViewModel
class ElectionsViewModel @Inject constructor(
	@RepositoryDataSource private val electionRepository: IElectionDataSource
) : ViewModel() {

//	private val savedElectionsTest = listOf(
//		Election(
//			1, "VIP Test Election", Calendar.getInstance().time, Division(
//				"div_1",
//				"PE",
//				"Lima"
//			)
//		),
//		Election(
//			2, "VIP Test Election", Calendar.getInstance().time, Division(
//				"div_2",
//				"PE",
//				"Lima"
//			)
//		),
//		Election(
//			3, "VIP Test Election", Calendar.getInstance().time, Division(
//				"div_3",
//				"PE",
//				"Lima"
//			)
//		),
//		Election(
//			4, "VIP Test Election", Calendar.getInstance().time, Division(
//				"div_4",
//				"PE",
//				"Lima"
//			)
//		),
//	)

	private val _upcomingElections = MutableLiveData<List<Election>>()
	val upcomingElections: LiveData<List<Election>> = _upcomingElections

	private val _savedElections: LiveData<List<Election>> = electionRepository.observeSavedElections()
	val savedElections: LiveData<List<Election>> = _savedElections

	//TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
	init {
		loadUpcomingElections()
	}

	private fun loadUpcomingElections() {
		viewModelScope.launch {
			_upcomingElections.value = electionRepository.getRemoteElections()
		}
	}

	//TODO: Create functions to navigate to saved or upcoming election voter info
}