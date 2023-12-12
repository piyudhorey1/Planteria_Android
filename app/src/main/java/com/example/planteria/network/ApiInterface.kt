package com.example.planteria.network

import com.example.planteria.responseModel.GetSpeciesDataResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("/api/species-list?key=sk-mORS6576a2fcdd31a3417&page=1")
    fun getSpeciesData() : Call<GetSpeciesDataResponse>
}