package com.example.planteria.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planteria.R
import com.example.planteria.activity.CaptureImageActivity
import com.example.planteria.databinding.FragmentWaterMeBinding

class WaterMeFragment : Fragment() {

    lateinit var binding: FragmentWaterMeBinding
    lateinit var captureImageActivity: CaptureImageActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWaterMeBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

    companion object {

        fun newInstance(captureActivity: CaptureImageActivity) =
            WaterMeFragment().apply {
                arguments = Bundle().apply {
                    captureImageActivity = captureActivity
                }
            }
    }
}