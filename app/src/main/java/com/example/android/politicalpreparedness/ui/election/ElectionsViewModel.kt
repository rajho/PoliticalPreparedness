package com.example.android.politicalpreparedness.ui.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.di.RepositoryDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

//TODO: Construct ViewModel and provide election datasource
@HiltViewModel
class ElectionsViewModel @Inject constructor(
	@RepositoryDataSource private val electionRepository: IElectionDataSource
) : ViewModel() {

	private val _upcomingElections = MutableLiveData<List<Election>>()
	val upcomingElections: LiveData<List<Election>> = _upcomingElections

	private val _savedElections: LiveData<List<Election>> = electionRepository.observeSavedElections()
	val savedElections: LiveData<List<Election>> = _savedElections

	private val _loadingUpcomingElections = MutableLiveData<Boolean>()
	val loadingUpcomingElections: LiveData<Boolean> = _loadingUpcomingElections

	private val _emptySavedElections : LiveData<Boolean> = Transformations.map(_savedElections) {
		it.isNullOrEmpty()
	}
	val emptySavedElections: LiveData<Boolean> = _emptySavedElections

	private val _showConnectionError = MutableLiveData<Boolean>()
	val showConnectionError: LiveData<Boolean> = _showConnectionError

	//TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
	init {
		loadUpcomingElections()
	}

	private fun loadUpcomingElections() {
		_loadingUpcomingElections.value = true
		viewModelScope.launch {
			try {
				_upcomingElections.value = electionRepository.getRemoteElections()
			} catch (e: Exception) {
				if (e is UnknownHostException) {
					_showConnectionError.value = true
				}
				e.printStackTrace()
			} finally {
				_loadingUpcomingElections.value = false
			}

		}
	}

	//TODO: Create functions to navigate to saved or upcoming election voter info
	// Navigation handled in the fragment
}