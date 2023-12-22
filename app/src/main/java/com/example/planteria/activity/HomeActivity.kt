package com.example.planteria.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.planteria.R
import com.example.planteria.databinding.ActivityHomeBinding
import com.example.planteria.fragment.AddedPlantsFragment
import com.example.planteria.fragment.HomeFragment
import com.google.android.material.tabs.TabLayout

class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding

    var strHome:String = "home"
    var strSearch:String = "search"
    var strAdd:String = "add"
    var strReminder:String = "reminder"
    var strProfile:String = "profile"

    private val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_home_selected).setTag(strHome))
//        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_grid_search).setTag(strSearch))
        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_add_image).setTag(strAdd))
        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_reminder).setTag(strReminder))
        binding.layoutTabs.addTab(binding.layoutTabs.newTab().setIcon(R.drawable.ic_user).setTag(strProfile))


        replaceSelectedFragment(HomeFragment.newInstance(this@HomeActivity))

        val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when(tab?.tag) {
                    strHome -> {
                        replaceSelectedFragment(HomeFragment.newInstance(this@HomeActivity))
                        tab.setIcon(R.drawable.ic_home_selected)
                    }
                    strSearch -> {
                        tab.setIcon(R.drawable.ic_grid_search_selected)
                    }
                    strAdd -> {
                        replaceSelectedFragment(AddedPlantsFragment())
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

   fun replaceSelectedFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layoutOtherTabs, fragment!!).addToBackStack(null).commit()

    }

   fun addNewFragment(fragment: Fragment?, tag:String, bundle: Bundle) {
        fragment?.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutOtherTabs, fragment!!,tag)
            .setCustomAnimations(
                R.anim.slide_up,
                0,
                0,
                0
            )
            .addToBackStack(null).commit()
    }

}