package com.example.planteria.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.planteria.R
import com.example.planteria.adapter.OnBoardingAdapter
import com.example.planteria.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {

//    lateinit var binding: ActivityOnBoardingBinding
    lateinit var adapter: OnBoardingAdapter
    lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_on_boarding)

        val layouts = intArrayOf(
            R.layout.activity_on_boarding_1,
            R.layout.activity_on_boarding_2,
            R.layout.activity_on_boarding_3
        )

        viewPager = findViewById(R.id.viewPager)
        adapter = OnBoardingAdapter(this, layouts)
        viewPager.adapter = adapter


        val btnNext = findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            val current = viewPager.currentItem + 1
            if (current < layouts.size) {
                viewPager.currentItem = current
            } else {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
        }

    }
}