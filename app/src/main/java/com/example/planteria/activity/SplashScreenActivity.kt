package com.example.planteria.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.planteria.R
import com.example.planteria.PlanteriaApplication
import com.example.planteria.utils.PrefHelper

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper())
            .postDelayed({
                if (PlanteriaApplication.prefHelper.getString(PrefHelper.TOKEN).equals("")) {
                    openOnBoardingActivity()
                } else {
                    openHomeScreen()
                }
            }, 2000)
    }


    private fun openHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openOnBoardingActivity() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}