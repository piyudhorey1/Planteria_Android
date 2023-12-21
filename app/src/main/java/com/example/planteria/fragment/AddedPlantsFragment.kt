package com.example.planteria.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planteria.R
import com.example.planteria.activity.HomeActivity
import com.example.planteria.databinding.FragmentAddedPlantsBinding

class AddedPlantsFragment : Fragment() {

    lateinit var binding: FragmentAddedPlantsBinding
    lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddedPlantsBinding.inflate(layoutInflater, container, false)
        if (context is HomeActivity) {
            homeActivity = context as HomeActivity
        }

        binding.btnAddPlantData.setOnClickListener {
            homeActivity.addNewFragment(AddPlantFragment.newInstance(), "Add plant", Bundle())
        }

        return binding.root
    }

    companion object {
        fun newInstance() =
            AddedPlantsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}