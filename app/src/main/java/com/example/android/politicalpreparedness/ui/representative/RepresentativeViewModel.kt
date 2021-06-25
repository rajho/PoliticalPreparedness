package com.example.android.politicalpreparedness.ui.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.IElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.di.RepositoryDataSource
import com.example.android.politicalpreparedness.ui.representative.model.Representative
import com.example.android.politicalpreparedness.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class RepresentativeViewModel @Inject constructor(
	@RepositoryDataSource private val electionRepository: IElectionDataSource
) : ViewModel() {

	val states: MutableList<String> = mutableListOf()

	var stateSelected: String = String()
	var address1 = MutableLiveData<String>()
	var address2 = MutableLiveData<String>()
	var city = MutableLiveData<String>()
	var zip = MutableLiveData<String>()

	val addressFromGeoLocation = MutableLiveData<Address>()

	//TODO: Establish live data for representatives and address
	private val _representatives = MutableLiveData<List<Representative>>()
	val representatives: LiveData<List<Representative>> = _representatives

	private val _useMyLocation = SingleLiveEvent<Boolean>()
	val useMyLocation: SingleLiveEvent<Boolean> = _useMyLocation

	val showToastInt: SingleLiveEvent<Int> = SingleLiveEvent()

	private val _showConnectionError = MutableLiveData<Boolean>()
	val showConnectionError: LiveData<Boolean> = _showConnectionError

	private val _loadingRepresentatives = MutableLiveData<Boolean>()
	val loadingRepresentatives: LiveData<Boolean> = _loadingRepresentatives

	/**
	 *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

	val (offices, officials) = getRepresentativesDeferred.await()
	_representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

	Note: getRepresentatives in the above code represents the method used to fetch data from the API
	Note: _representatives in the above code represents the established mutable live data housing representatives

	 */

	fun useMyLocation() {
		_useMyLocation.value = true
	}

	//TODO: Create function get address from geo location
	private fun getAddressFromGeoLocation(): String {
		addressFromGeoLocation.value?.let {
			stateSelected = it.state
			address1.value = it.line1
			address2.value = it.line2 ?: ""
			city.value = it.city
			zip.value = it.zip
		}

		return addressFromGeoLocation.value?.toFormattedString() ?: ""
	}

	//TODO: Create function to get address from individual fields
	private fun getFullAddress(): String {
		return """${address1.value ?: ""} ${address2.value ?: ""} ${city.value ?: ""} $stateSelected ${zip.value ?: ""}"""
	}

	//TODO: Create function to fetch representatives from API from a provided address
	fun findMyRepresentatives(useLocation: Boolean) {
		_loadingRepresentatives.value = true

		val address = if (useLocation) {
			getAddressFromGeoLocation()
		} else {
			getFullAddress()
		}

		viewModelScope.launch {
			try {
				_representatives.value = electionRepository.getRepresentatives(address)
			} catch (e:Exception) {
				when(e) {
					is UnknownHostException -> {
						_showConnectionError.value = true
						showToastInt.value = R.string.connection_error
					}
					is HttpException -> showToastInt.value = R.string.not_found_error_404
				}
			} finally {
				_loadingRepresentatives.value = false
			}
			_showConnectionError.value = false
		}
	}

}
