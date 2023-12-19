package com.example.planteria.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.planteria.R
import com.example.planteria.activity.HomeActivity
import com.example.planteria.databinding.FragmentKnowAboutPlantBinding
import com.example.planteria.databinding.FragmentLearnAboutPlantBinding
import com.example.planteria.network.ApiInterface
import com.example.planteria.network.RetrofitClient
import com.example.planteria.responseModel.GetSpeciesDataResponse
import com.example.planteria.utils.LoadingDialogFragment
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LearnAboutPlantFragment : Fragment() {

    lateinit var binding: FragmentLearnAboutPlantBinding
    lateinit var homeActivity: HomeActivity
    var mLoadingFragment = LoadingDialogFragment()
    private var plantId : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLearnAboutPlantBinding.inflate(layoutInflater, container, false)


        hitGetSpeciesDataApi(plantId)

        return binding.root
    }

    private fun hitGetSpeciesDataApi(plantMainId : Int) {
        if (!mLoadingFragment.isAdded) {
            mLoadingFragment.show(childFragmentManager, "")
        }
        val apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

        apiInterface.getSpeciesData(plantMainId).enqueue(object : Callback<GetSpeciesDataResponse> {
            override fun onResponse(
                call: Call<GetSpeciesDataResponse>,
                response: Response<GetSpeciesDataResponse>
            ) {
                mLoadingFragment.dismissAllowingStateLoss()
                if (response.isSuccessful) {

                    val speciesData = response.body()
                    Glide
                        .with(homeActivity.applicationContext)
                        .load(speciesData?.defaultImage?.originalUrl)
                        .centerCrop()
                        .into(binding.imgRestPicture)
                    binding.txtPlantName.setText(speciesData?.commonName)
                    binding.txtDescription.setText(speciesData?.description)
                    binding.txtFamily.setText(speciesData?.family)
                    binding.txtOrigin.setText(speciesData?.origin.toString())
                }
            }

            override fun onFailure(call: Call<GetSpeciesDataResponse>, t: Throwable) {
                mLoadingFragment.dismissAllowingStateLoss()
                println("Error")
            }

        })

    }

    companion object {
        fun newInstance(homeScreenActivity: HomeActivity, PlantId: Int) =
            LearnAboutPlantFragment().apply {
                arguments = Bundle().apply {
                    homeActivity = homeScreenActivity
                    plantId = PlantId
                }
            }
    }
}