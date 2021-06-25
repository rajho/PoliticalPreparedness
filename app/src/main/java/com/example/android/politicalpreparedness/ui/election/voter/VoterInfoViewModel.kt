package com.example.android.politicalpreparedness.ui.election.voter

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.di.RepositoryDataSource
import com.example.android.politicalpreparedness.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class VoterInfoViewModel @Inject constructor(
	@RepositoryDataSource private val electionRepository: IElectionDataSource,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	//TODO: Add live data to hold voter info
	private val _voterInfo = MutableLiveData<VoterInfoResponse>()
	val voterInfo: LiveData<VoterInfoResponse> = _voterInfo

	private val _isSavedElection = MutableLiveData<Boolean?>(null)
	val isSavedElection: LiveData<Boolean?> = _isSavedElection

	private val _election = MutableLiveData<Election>()
	val election: LiveData<Election> = _election

	private val _openBrowser = SingleLiveEvent<String>()
	val openBrowser: SingleLiveEvent<String> = _openBrowser

	val locationUrlAvailable = Transformations.map(voterInfo) {
		it?.state?.firstOrNull()?.electionAdministrationBody?.votingLocationFinderUrl != null
	}
	val ballotInformationUrlAvailable = Transformations.map(voterInfo) {
		it?.state?.firstOrNull()?.electionAdministrationBody?.ballotInfoUrl != null
	}

	val showToastInt: SingleLiveEvent<Int> = SingleLiveEvent()

	private val _brokenVoterInfo = MutableLiveData<Boolean>()
	val brokenVoterInfo: LiveData<Boolean> = _brokenVoterInfo

	private val _loading = MutableLiveData<Boolean>()
	val loading: LiveData<Boolean> = _loading

	var electionId: Int? = null
	var division: Division? = null

	private val _showConnectionError = MutableLiveData<Boolean>()
	val showConnectionError: LiveData<Boolean> = _showConnectionError

	//TODO: Add var and methods to populate voter info
	init {
		electionId = savedStateHandle["arg_election_id"]
		division = savedStateHandle["arg_division"]

		loadVoterInfo()
		checkIsSavedElection()
	}

	private fun loadVoterInfo() {
		_loading.value = true
		viewModelScope.launch {
			if (electionId != null && division != null) {
				try {
					val address = "${division?.state}, ${division?.country}"
					_voterInfo.value = electionRepository.getVoterInfo(electionId!!, address)
					_voterInfo.value?.election?.let { it -> _election.value = it}
				} catch (e: Exception) {
					 when(e) {
						is HttpException -> _brokenVoterInfo.value = true
						is UnknownHostException -> _showConnectionError.value = true
						 else -> _brokenVoterInfo.value = true
					 }
				} finally {
					_loading.value = false
				}
			} else {
				showToastInt.value = R.string.toast_missing_information
			}
		}
	}

	//TODO: Add var and methods to support loading URLs
	fun openLocationsInBrowser() {
		voterInfo.value?.state?.firstOrNull()?.electionAdministrationBody?.votingLocationFinderUrl?.let {
			_openBrowser.value = it
		}
	}

	fun openBallotInformationInBrowser() {
		voterInfo.value?.state?.firstOrNull()?.electionAdministrationBody?.ballotInfoUrl?.let {
			_openBrowser.value = it
		}
	}


	//TODO: Add var and methods to save and remove elections to local database
	//TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
	/**
	 * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
	 */
	private fun checkIsSavedElection() {
		viewModelScope.launch {
			electionId?.let {
				_isSavedElection.value = electionRepository.getElectionById(it) != null
			}
		}
	}

	fun toogleSavedElection(){
		_isSavedElection.value?.let { isSaved ->
			viewModelScope.launch {
				if (isSaved) {
					_election.value?.let { electionRepository.deleteSavedElection(it) }
					_isSavedElection.value = false
				} else {
					_election.value?.let { electionRepository.saveElection(it) }
					_isSavedElection.value = true
				}
			}
		}
	}

}