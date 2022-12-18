package aut.dipterv.word_gardner.ui.practice.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import aut.dipterv.word_gardner.network_data.interceptors.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    fun logout(context: Context) {
        val sessionManager = SessionManager(context)
        sessionManager.saveAuthToken("")
    }

    fun setIsOnline(context: Context, isOnline: Boolean) {
        val sessionManager = SessionManager(context)
        sessionManager.saveIsOnline(isOnline)
    }

    fun getIsOnline(context: Context): Boolean {
        val sessionManager = SessionManager(context)
        return sessionManager.fetchIsOnline()
    }
}
