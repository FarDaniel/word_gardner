package aut.dipterv.word_gardner.network_data.models.dtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchFilter(
    @SerializedName("name_part")
    val namePart: String? = null,
    @SerializedName("category_part")
    val categoryPart: String? = null,
    @SerializedName("minimal_upvote_percentage")
    val minimalUpvotePercentage: Double = 0.0,
    @SerializedName("user_id")
    val userId: Long? = null,
    @SerializedName("easy_included")
    val easyIncluded: Boolean = false,
    @SerializedName("medium_included")
    val mediumIncluded: Boolean = false,
    @SerializedName("hard_included")
    val hardIncluded: Boolean = false
) : Parcelable {
    fun generateForUser(userId: Long): SearchFilter {
        return SearchFilter(
            namePart = namePart,
            categoryPart = categoryPart,
            minimalUpvotePercentage = minimalUpvotePercentage,
            userId = userId,
            easyIncluded = easyIncluded,
            mediumIncluded = mediumIncluded,
            hardIncluded = hardIncluded
        )
    }
}
