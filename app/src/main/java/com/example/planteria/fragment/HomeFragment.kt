package com.example.planteria.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.planteria.PlanteriaApplication
import com.example.planteria.R
import com.example.planteria.activity.HomeActivity
import com.example.planteria.activity.SignInActivity
import com.example.planteria.adapter.PlantsImageHorizontalAdapter
import com.example.planteria.adapter.PlantsNameHorizontalAdapter
import com.example.planteria.databinding.FragmentHomeBinding
import com.example.planteria.network.ApiInterface
import com.example.planteria.network.RetrofitClient
import com.example.planteria.responseModel.GetPlantsDataResponse
import com.example.planteria.responseModel.SpeciesData
import com.example.planteria.utils.LoadingDialogFragment
import com.example.planteria.utils.PrefHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var homeActivity: HomeActivity
    var mLoadingFragment = LoadingDialogFragment()
    lateinit var plantsImageHorizontalAdapter: PlantsImageHorizontalAdapter
    private var plantsList: MutableList<SpeciesData> = mutableListOf()

    private lateinit var actionBarToggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        homeActivity = activity as HomeActivity

        hitGetAllPlantsDataApi()

        actionBarToggle = ActionBarDrawerToggle(homeActivity, binding.drawerLayout, 0, 0)
        binding.drawerLayout.addDrawerListener(actionBarToggle)

        actionBarToggle.syncState()

        homeActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.imgSwipe.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.navView.setNavigationItemSelectedListener {menuItem ->
            when (menuItem.itemId) {
                R.id.myHome -> {
                    Toast.makeText(homeActivity, "My Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.reminder -> {
                    Toast.makeText(homeActivity, "My Reminders", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.add -> {
                    Toast.makeText(homeActivity, "Add My Plants", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.profile -> {
                    Toast.makeText(homeActivity, "My Profile", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.logOut -> {
                    clearUserData()
                    true
                }
                else ->  {
                    false
                }
            }

        }

        binding.txtUserName.setText(
            "Hi "+ PlanteriaApplication.prefHelper.getString(PrefHelper.USER_DETAIL)
        )

        return binding.root
    }

    private fun clearUserData() {
        PlanteriaApplication.prefHelper.clear()
        val intent = Intent(homeActivity, SignInActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun hitGetAllPlantsDataApi() {
        if (!mLoadingFragment.isAdded) {
            mLoadingFragment.show(childFragmentManager, "")
        }
        val apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

        lifecycleScope.launch {

            apiInterface.getPlantsData().enqueue(object :
            Callback<GetPlantsDataResponse>{
                override fun onResponse(
                    call: Call<GetPlantsDataResponse>,
                    response: Response<GetPlantsDataResponse>
                ) {
                    mLoadingFragment.dismissAllowingStateLoss()

                    if (response.isSuccessful) {

                        val getPlantsData = response.body()

                        setHorizontalPlantsImages(getPlantsData!!)
                        setHorizontalPlantsName(getPlantsData)
                    }
                }

                override fun onFailure(call: Call<GetPlantsDataResponse>, t: Throwable) {
                    mLoadingFragment.dismissAllowingStateLoss()
                }

            })
        }
    }

    private fun setHorizontalPlantsImages(mAllPlantsData: GetPlantsDataResponse?) {

        binding.rcyImgPlants.apply {
            layoutManager = LinearLayoutManager(homeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = PlantsImageHorizontalAdapter(
                homeActivity,
                mAllPlantsData!!,
                object : PlantsImageHorizontalAdapter.OnItemClickListener {
                    override fun onItemClick(plaintId: Int) {
                        openKnowAboutPlantFragment(plaintId)
                    }
                }
            )
        }
    }

    private fun openKnowAboutPlantFragment(plantId : Int) {
//        homeActivity.replaceSelectedFragment(KnowAboutPlantFragment.newInstance(homeActivity, plantId))
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            R.anim.fragment_enter_animation,
            R.anim.fragment_exit_animation
        )

        transaction.replace(R.id.layoutOtherTabs, KnowAboutPlantFragment.newInstance(homeActivity, plantId))
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun setHorizontalPlantsName(mAllPlantsData: GetPlantsDataResponse?) {
        binding.rcyNamePlants.apply {
            layoutManager = LinearLayoutManager(homeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = PlantsNameHorizontalAdapter(
                homeActivity,
                mAllPlantsData!!,
                object : PlantsNameHorizontalAdapter.OnItemClickListener {
                    override fun onItemClick(plaintId: Int) {
                        homeActivity.replaceSelectedFragment(LearnAboutPlantFragment.newInstance(homeActivity, plaintId))
                    }
                }
            )
        }
    }



    companion object {
        fun newInstance(homeScreenActivity: HomeActivity) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    homeActivity = homeScreenActivity
                }
            }
    }
}