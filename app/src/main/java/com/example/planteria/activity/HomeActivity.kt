package com.example.planteria.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.planteria.R
import com.example.planteria.databinding.ActivityHomeBinding
import com.example.planteria.fragment.CaptureFragment
import com.example.planteria.fragment.HomeFragment
import com.google.android.material.tabs.TabLayout

class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding

    var strHome:String = "home"
    var strSearch:String = "search"
    var strReminder:String = "reminder"
    var strProfile:String = "profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_home_selected).setTag(strHome))
        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_grid_search).setTag(strSearch))
        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_reminder).setTag(strReminder))
        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_user).setTag(strProfile))

        binding.imgCaptureClosed.setOnClickListener {
//            if (binding.imgCaptureOpened.visibility == View.VISIBLE) {
//                binding.imgCaptureOpened.visibility = View.GONE
//
//            }else {
//                binding.imgCaptureOpened.visibility = View.VISIBLE
//            }
            addNewFragment(CaptureFragment.newInstance(this@HomeActivity), "", Bundle())
        }

        replaceSelectedFragment(HomeFragment.newInstance(this@HomeActivity))

        val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when(tab?.tag) {
                    strHome -> {
                        tab.setIcon(R.drawable.ic_home_selected)
                    }
                    strSearch -> {
                        tab.setIcon(R.drawable.ic_grid_search_selected)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

                when(tab?.tag) {
                    strHome -> {
                        tab.setIcon(R.drawable.ic_home)
                    }
                    strSearch -> {
                        tab.setIcon(R.drawable.ic_grid_search)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }

        binding.layoutTabs.addOnTabSelectedListener(onTabSelectedListener)
    }

   private fun replaceSelectedFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layoutOtherTabs, fragment!!).addToBackStack(null).commit()

    }

    private fun addNewFragment(fragment: Fragment?, tag:String, bundle: Bundle) {
        fragment?.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutOtherTabs, fragment!!,tag).addToBackStack(null).commit()
    }
}