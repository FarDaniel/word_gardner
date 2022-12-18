package aut.dipterv.word_gardner.network_data.models

import com.google.gson.annotations.SerializedName

data class VoteModel(
    @SerializedName("id")
    val id: Long,
    @SerializedName("from_user")
    var fromUser: Long,
    @SerializedName("to_user")
    var toUser: Long,
    @SerializedName("is_upvote")
    var isUpvote: Boolean,
    @SerializedName("on_folder")
    var onFolder: Long?,
    @SerializedName("on_pack")
    var onPack: Long?
)
