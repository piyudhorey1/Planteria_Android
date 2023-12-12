package com.example.planteria.responseModel

import com.google.gson.annotations.SerializedName

data class GetSpeciesDataResponse (

    @SerializedName("data"         ) var data        : ArrayList<SpeciesData> = arrayListOf(),
    @SerializedName("to"           ) var to          : Int?            = null,
    @SerializedName("per_page"     ) var perPage     : Int?            = null,
    @SerializedName("current_page" ) var currentPage : Int?            = null,
    @SerializedName("from"         ) var from        : Int?            = null,
    @SerializedName("last_page"    ) var lastPage    : Int?            = null,
    @SerializedName("total"        ) var total       : Int?            = null

)