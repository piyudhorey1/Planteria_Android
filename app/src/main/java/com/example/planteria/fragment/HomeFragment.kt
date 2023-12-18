package com.example.planteria.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planteria.activity.HomeActivity
import com.example.planteria.adapter.PlantsImageHorizontalAdapter
import com.example.planteria.adapter.PlantsNameHorizontalAdapter
import com.example.planteria.databinding.FragmentHomeBinding
import com.example.planteria.network.ApiInterface
import com.example.planteria.network.RetrofitClient
import com.example.planteria.responseModel.GetPlantsDataResponse
import com.example.planteria.responseModel.SpeciesData
import com.example.planteria.utils.LoadingDialogFragment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        homeActivity = activity as HomeActivity

        hitGetAllPlantsDataApi()


        return binding.root
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
                mAllPlantsData!!
            )
        }
    }

    private fun setHorizontalPlantsName(mAllPlantsData: GetPlantsDataResponse?) {
        binding.rcyNamePlants.apply {
            layoutManager = LinearLayoutManager(homeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = PlantsNameHorizontalAdapter(
                homeActivity,
                mAllPlantsData!!
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