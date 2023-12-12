package com.example.planteria.responseModel

import com.google.gson.annotations.SerializedName

data class PlantsData(

    @SerializedName("plant data ") var plantData    : ArrayList<SpeciesData> = arrayListOf()

)
