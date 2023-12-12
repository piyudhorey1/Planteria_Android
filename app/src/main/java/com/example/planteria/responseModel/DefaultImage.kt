package com.example.planteria.responseModel

import com.google.gson.annotations.SerializedName

data class DefaultImage(

    @SerializedName("license"      ) var license     : Int?    = null,
    @SerializedName("license_name" ) var licenseName : String? = null,
    @SerializedName("license_url"  ) var licenseUrl  : String? = null,
    @SerializedName("original_url" ) var originalUrl : String? = null,
    @SerializedName("regular_url"  ) var regularUrl  : String? = null,
    @SerializedName("medium_url"   ) var mediumUrl   : String? = null,
    @SerializedName("small_url"    ) var smallUrl    : String? = null,
    @SerializedName("thumbnail"    ) var thumbnail   : String? = null

)
