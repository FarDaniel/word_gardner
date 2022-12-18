package aut.dipterv.word_gardner_backend.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @param namePart If we're filtering by name, the name should contain this.
 * @param categoryPart If we're filtering by category, it should contain this.
 * @param userId If we search by user, we need this identifier.\
 * @param minimalUpvote We can set a minimal upvote count, this way we use this number.
 * @param minimalUpvotePercentage The required rate between up and down votes in percentage
 * @param difficulties A filter for the contained folders and packs difficulty
 */
data class SearchFilter(
    @field:JsonProperty("name_part") val namePart: String = "",

    @field:JsonProperty("category_part") val categoryPart: String = "",

    @field:JsonProperty("user_id") val userId: Long = -1,

    @field:JsonProperty("minimal_upvote_percentage") val minimalUpvotePercentage: Float,

    @field:JsonProperty("easy_included") val easyIncluded: Boolean = false,

    @field:JsonProperty("medium_included") val mediumIncluded: Boolean = false,

    @field:JsonProperty("hard_included") val hardIncluded: Boolean = false
)

