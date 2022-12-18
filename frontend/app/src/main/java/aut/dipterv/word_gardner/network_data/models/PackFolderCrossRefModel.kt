package aut.dipterv.word_gardner.network_data.models

import com.google.gson.annotations.SerializedName

data class PackFolderCrossRefModel(
    @SerializedName("id") val id: Long,
    @SerializedName("pack_id") val packId: Long,
    @SerializedName("folder_id") val folderId: Long
)