package com.example.android.politicalpreparedness.ui.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.ui.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.ui.representative.adapter.setNewValue
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RepresentativeFragment : Fragment(), AdapterView.OnItemSelectedListener {

	companion object {
		//TODO: Add Constant for Location request
		private val TAG = RepresentativeFragment::class.java.simpleName

		private const val FINE_LOCATION_REQUEST_CODE = 44
		private const val FINE_LOCATION_PERMISSION_INDEX = 0
		private const val REQUEST_TURN_DEVICE_LOCATION_ON = 77
	}

	private lateinit var binding: FragmentRepresentativeBinding
	private lateinit var fusedLocationProvideClient: FusedLocationProviderClient

	//TODO: Declare ViewModel
	private val _viewModel: RepresentativeViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		//TODO: Establish bindings
		binding = DataBindingUtil
			.inflate(inflater, R.layout.fragment_representative, container, false)
		binding.viewModel = _viewModel
		binding.lifecycleOwner = this
		binding.state.onItemSelectedListener = this
		_viewModel.states.addAll(resources.getStringArray(R.array.states).toList())

		//TODO: Define and assign Representative adapter
		val representativeAdapter = RepresentativeListAdapter()
		binding.recyclerViewRepresentatives.adapter = representativeAdapter

		//TODO: Populate Representative adapter
		_viewModel.representatives.observe(viewLifecycleOwner, {
			hideKeyboard()
			it?.let {
				representativeAdapter.submitList(it)
			}
		})

		//TODO: Establish button listeners for field and location search
		_viewModel.useMyLocation.observe(viewLifecycleOwner, {
			if (it) {
				checkLocationPermissionAndSetAddress()
			}
		})

		_viewModel.showToastInt.observe(viewLifecycleOwner, {
			Toast.makeText(activity, getString(it), Toast.LENGTH_LONG).show()
		})

		return binding.root
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		//TODO: Handle location permission result to get location on permission granted
		val fineLocationDenied = grantResults[FINE_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED

		if (grantResults.isEmpty() || fineLocationDenied) {
			Snackbar
				.make(
					binding.buttonLocation,
					R.string.permission_denied_explanation,
					Snackbar.LENGTH_INDEFINITE
				)
				.setAction(R.string.settings) {
					startActivity(Intent().apply {
						action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
						data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
						flags = Intent.FLAG_ACTIVITY_NEW_TASK
					})
				}
				.show()
		} else {
			checkDeviceLocationEnabledAndSetAddress()
		}
	}

	private fun checkLocationPermissionAndSetAddress() {
		return if (isPermissionGranted()) {
			checkDeviceLocationEnabledAndSetAddress()
		} else {
			//TODO: Request Location permissions
			val permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
			val resultCode = FINE_LOCATION_REQUEST_CODE
			requestPermissions(permissionsArray, resultCode)
		}
	}

	private fun checkDeviceLocationEnabledAndSetAddress(resolve: Boolean = true) {
		val locationRequest = LocationRequest.create().apply {
			priority = LocationRequest.PRIORITY_LOW_POWER
		}
		val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

		context?.let { ctx ->
			val settingsClient = LocationServices.getSettingsClient(ctx)
			val locationSettingsReponseTask = settingsClient.checkLocationSettings(builder.build())

			// if location was NOT enabled on device
			locationSettingsReponseTask.addOnFailureListener { exception ->
				if (exception is ResolvableApiException && resolve) {
					try {
						startIntentSenderForResult(
							exception.resolution.intentSender,
							REQUEST_TURN_DEVICE_LOCATION_ON,
							null,
							0,
							0,
							0,
							null
						)
					} catch (sendEx: IntentSender.SendIntentException) {
						Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
					}
				} else {
					Snackbar
						.make(
							binding.buttonLocation,
							R.string.location_required_error,
							Snackbar.LENGTH_INDEFINITE
						)
						.setAction(android.R.string.ok) {
							checkDeviceLocationEnabledAndSetAddress()
							it.visibility = View.GONE
						}.show()
				}
			}

			// if location was enabled on device
			locationSettingsReponseTask.addOnCompleteListener {
				if (it.isSuccessful) {
					getLocation()
				}
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {

			// Setting current location right after granting Location Permission
			if (Activity.RESULT_OK == resultCode) {
				val locationCallback = object : LocationCallback() {
					override fun onLocationResult(locationResult: LocationResult) {
						getLocation()
					}
				}

				with(LocationRequest()) {
					priority = LocationRequest.PRIORITY_HIGH_ACCURACY
					interval = 0
					fastestInterval = 0
					numUpdates = 1
					fusedLocationProvideClient.requestLocationUpdates(
						this,
						locationCallback,
						Looper.myLooper()
					)
				}
			} else {
				checkDeviceLocationEnabledAndSetAddress(false)
			}
		}
	}

	private fun isPermissionGranted(): Boolean {
		//TODO: Check if permission is already granted and return (true = granted, false = denied/other)
		val fineLocationPermission = ContextCompat.checkSelfPermission(
			requireContext(),
			Manifest.permission.ACCESS_FINE_LOCATION
		)
		return fineLocationPermission == PackageManager.PERMISSION_GRANTED
	}

	@SuppressLint("MissingPermission")
	private fun getLocation() {
		//TODO: Get location from LocationServices
		fusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(requireContext())
		//TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
		val locationResult = fusedLocationProvideClient.lastLocation
		locationResult.addOnCompleteListener { task ->
			if (task.isSuccessful) {
				val lastKnownLocation = task.result
				lastKnownLocation?.run {
					val address = geoCodeLocation(this)
					binding.state.setNewValue(address.state)
					_viewModel.addressFromGeoLocation.value = address
					_viewModel.findMyRepresentatives(true)
				}
			}
		}

	}

	private fun geoCodeLocation(location: Location): Address {
		val geocoder = Geocoder(context, Locale.getDefault())
		return geocoder.getFromLocation(location.latitude, location.longitude, 1)
			.map { address ->
				Address(
					address.thoroughfare ?: "",
					address.subThoroughfare,
					address.locality ?: "",
					address.adminArea ?: "",
					address.postalCode ?: ""
				)
			}
			.first()
	}

	private fun hideKeyboard() {
		val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.hideSoftInputFromWindow(requireView().windowToken, 0)
	}

	override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
		binding.viewModel?.stateSelected = parent?.getItemAtPosition(position) as String
	}

	override fun onNothingSelected(parent: AdapterView<*>?) {
		binding.viewModel?.stateSelected = "Alabama"
	}

}