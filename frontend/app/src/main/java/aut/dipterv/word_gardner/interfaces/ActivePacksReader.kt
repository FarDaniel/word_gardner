package aut.dipterv.word_gardner.interfaces

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.PackSelectorFragment

interface ActivePacksReader {

    var navController: NavController?
    var fallbackDirection: NavDirections

    fun getActivePacks(context: Context): ArrayList<Long> {
        val prefs =
            context.getSharedPreferences(PackSelectorFragment.ACTIVE_PACK_IDS, Context.MODE_PRIVATE)
        val ids = prefs.getString(PackSelectorFragment.ACTIVE_PACK_IDS, null)
        if (ids.isNullOrEmpty()) {
            navController?.navigate(fallbackDirection)
        } else {
            return parseArrayListString(ids)
        }
        return ArrayList()
    }

    private fun parseArrayListString(string: String?): ArrayList<Long> {
        val response = ArrayList<Long>()
        if (!string.isNullOrEmpty()) {
            var rawData = string
            val rawLength = rawData.length
            rawData = rawData.replace("[", "")
            rawData = rawData.replace("]", "")
            if (rawData.length == (rawLength - 2)) {
                val numbers = rawData.split(", ")
                numbers.forEach { number ->
                    response.add(number.toLong())
                }
            }
        }
        return response
    }
}
