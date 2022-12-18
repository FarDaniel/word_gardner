package aut.dipterv.word_gardner.network_data.models

import com.google.gson.annotations.SerializedName

data class PictureUrlArray(
    @SerializedName("urls")
    val urls: SmallPictureUrl
)
