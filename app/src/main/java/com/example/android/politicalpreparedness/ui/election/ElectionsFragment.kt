package com.example.android.politicalpreparedness.ui.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.ui.election.voter.VoterInfoFragment
import com.example.android.politicalpreparedness.ui.launch.LaunchFragmentDirections

class ElectionsFragment: Fragment() {

    private lateinit var binding: FragmentElectionBinding

    //TODO: Declare ViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        binding.root.setOnClickListener {
            navToVoterInfo()
        }


        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters
        return binding.root
    }

    private fun navToVoterInfo() {
        val division = Division("1", "Perú", "LI")

        this.findNavController().navigate(ElectionsFragmentDirections
                                              .actionElectionsFragmentToVoterInfoFragment(0, division))
    }

    //TODO: Refresh adapters when fragment loads

}