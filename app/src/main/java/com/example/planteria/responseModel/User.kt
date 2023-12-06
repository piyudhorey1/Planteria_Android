package com.example.planteria.responseModel

import com.google.gson.annotations.SerializedName


data class User (

    @SerializedName("id"           ) var id           : String?  = null,
    @SerializedName("fullName"     ) var fullName     : String?  = null,
    @SerializedName("token"        ) var token        : String?  = null,
    @SerializedName("emailAddress" ) var emailAddress : String?  = null,
    @SerializedName("isVerified"   ) var isVerified   : Boolean? = null,
    @SerializedName("password" )     var password : String?  = null

)