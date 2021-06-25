package com.example.android.politicalpreparedness.ui.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.databinding.FragmentElectionsBinding
import com.example.android.politicalpreparedness.ui.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.ui.election.adapter.ElectionListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ElectionsFragment: Fragment() {

    private lateinit var binding: FragmentElectionsBinding

    //TODO: Declare ViewModel
    private val _viewModel: ElectionsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_elections, container, false)

        //TODO: Add ViewModel values and create ViewModel
        binding.viewModel = _viewModel

        //TODO: Add binding values
        binding.lifecycleOwner = this

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters
        val upcomingAdapter = ElectionListAdapter(ElectionListener { electionId, division ->
            navToVoterInfo(electionId, division)
        })
        binding.recyclerViewUpcoming.adapter = upcomingAdapter

        val savedAdapter = ElectionListAdapter(ElectionListener { electionId, division ->
            navToVoterInfo(electionId, division)
        })
        binding.recyclerViewSaved.adapter = savedAdapter

        //TODO: Populate recycler adapters
        _viewModel.upcomingElections.observe(viewLifecycleOwner, {
            it?.let {
                upcomingAdapter.submitList(it)
            }
        })

        _viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                savedAdapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun navToVoterInfo(electionId: Int, division: Division) {
        this.findNavController().navigate(ElectionsFragmentDirections
                                              .actionElectionsFragmentToVoterInfoFragment(electionId,
                                                                                          division))
    }

    //TODO: Refresh adapters when fragment loads

}