package com.ubaya.uts_advnative_160419072.models

import com.google.gson.annotations.SerializedName

data class Resto(
    val id: Int,
    val nama: String,
    val alamat: String,
    val photo: String,
    var rating: Float? = null,
    @SerializedName("rating_count") var ratingCount: Int? = null
)
