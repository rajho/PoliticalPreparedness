package com.example.android.politicalpreparedness.ui.election.voter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoterInfoFragment : Fragment() {

	private lateinit var binding: FragmentVoterInfoBinding
	private val _viewModel: VoterInfoViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)
		binding.viewModel = _viewModel
		binding.lifecycleOwner = this

		// Observe for LiveData changes
		_viewModel.showToastInt.observe(this, Observer {
			Toast.makeText(activity, getString(it), Toast.LENGTH_LONG).show()
		})

		_viewModel.openBrowser.observe(this, Observer { url ->
			url?.let {
				loadUrlIntent(it)
			}
		})

		return binding.root
	}

	private fun loadUrlIntent(url: String) {
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}
}