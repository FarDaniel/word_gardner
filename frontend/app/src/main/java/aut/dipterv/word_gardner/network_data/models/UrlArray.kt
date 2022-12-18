package aut.dipterv.word_gardner.network_data.models

import com.google.gson.annotations.SerializedName

data class UrlArray(
    @SerializedName("results")
    val results: List<PictureUrlArray>
)
