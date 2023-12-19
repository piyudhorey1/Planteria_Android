package com.example.planteria.network

import com.example.planteria.responseModel.GetPlantsDataResponse
import com.example.planteria.responseModel.GetSpeciesDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("/api/species-list?key=sk-mORS6576a2fcdd31a3417&page=1")
    fun getPlantsData() : Call<GetPlantsDataResponse>

    @GET("/api/species/details/{plant_id}?key=sk-mORS6576a2fcdd31a3417")
    fun getSpeciesData(@Path("plant_id") plantId: Int) : Call<GetSpeciesDataResponse>
}