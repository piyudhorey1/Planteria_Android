package com.example.planteria

import android.app.Application
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import androidx.core.content.res.ResourcesCompat
import com.example.planteria.utils.PrefHelper

class PlanteriaApplication : Application() {

    init {
        instance = this
    }
    companion object {
        private var instance: Application? = null
        private var isActive: Boolean = false
        lateinit var prefHelper: PrefHelper
//        lateinit var firebaseAnalytics: FirebaseAnalytics



        var mLoadingAnimDrawable: AnimationDrawable? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

    }

    override fun onCreate() {
        super.onCreate()

// Obtain the FirebaseAnalytics instance.
//        FirebaseApp.initializeApp(this);
//        firebaseAnalytics = Firebase.analytics
        prefHelper = PrefHelper(this)

        mLoadingAnimDrawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.app_loader_animation_list, theme
        ) as AnimationDrawable?

    }
}