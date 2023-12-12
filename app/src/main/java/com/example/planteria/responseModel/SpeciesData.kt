package com.example.planteria.responseModel

import com.google.gson.annotations.SerializedName

data class SpeciesData(

    @SerializedName("id"              ) var id             : Int?              = null,
    @SerializedName("common_name"     ) var commonName     : String?           = null,
    @SerializedName("scientific_name" ) var scientificName : ArrayList<String> = arrayListOf(),
    @SerializedName("other_name"      ) var otherName      : ArrayList<String> = arrayListOf(),
    @SerializedName("cycle"           ) var cycle          : String?           = null,
    @SerializedName("watering"        ) var watering       : String?           = null,
    @SerializedName("sunlight"        ) var sunlight       : ArrayList<String> = arrayListOf(),
    @SerializedName("default_image"   ) var defaultImage   : DefaultImage?     = DefaultImage()


)
