package aut.dipterv.word_gardner.network_data.models

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("name")
    val userName: String,
    @SerializedName("password")
    val password: String
)
