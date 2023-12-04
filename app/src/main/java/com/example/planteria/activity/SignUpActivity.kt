package com.example.planteria.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.planteria.R
import com.example.planteria.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}