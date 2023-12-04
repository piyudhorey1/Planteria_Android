package com.example.planteria.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.planteria.R
import com.example.planteria.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {

    lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}