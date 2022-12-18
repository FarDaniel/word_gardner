package aut.dipterv.word_gardner.network_data.models

import aut.dipterv.word_gardner.local_data.models.User
import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id")
    override val id: Long,
    @SerializedName("score")
    val score: Int,
    @SerializedName("up_votes")
    val upVotes: Int,
    @SerializedName("down_votes")
    val downVotes: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("picture")
    val picture: String?
) : BackendModel() {
    fun toLocal(): User {
        return User(
            onlineId = id,
            name = name,
            picture = picture
        )
    }
}
