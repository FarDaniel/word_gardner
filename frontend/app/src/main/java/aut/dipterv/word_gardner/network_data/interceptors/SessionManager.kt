package aut.dipterv.word_gardner.network_data.interceptors

import android.content.Context
import android.content.SharedPreferences
import aut.dipterv.word_gardner.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val IS_ONLINE = "is_online"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveIsOnline(isOnline: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(IS_ONLINE, isOnline)
        editor.apply()
    }

    fun fetchIsOnline(): Boolean {
        return prefs.getBoolean(IS_ONLINE, true)
    }

}
