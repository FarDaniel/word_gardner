package aut.dipterv.word_gardner

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aut.dipterv.word_gardner.network_data.interactors.UserInteractor
import aut.dipterv.word_gardner.network_data.interceptors.SessionManager
import aut.dipterv.word_gardner.network_data.models.LoginModel
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val userInteractor: UserInteractor) : ViewModel() {

    val loggedIn = MutableLiveData(Unit)
    val error = MutableLiveData<Int>()

    fun login(user: LoginModel, context: Context) {
        val sessionManager = SessionManager(context)
        val loginDisposable = userInteractor.login(user).subscribe({
            sessionManager.saveAuthToken(it.token)
            loggedIn.value = Unit
        }, { exception ->
            error.value = (exception as HttpException).code()
        })
    }

    @SuppressLint("CheckResult")
    fun register(user: LoginModel, context: Context) {
        val sessionManager = SessionManager(context)
        val registerDisposable = userInteractor.register(user).subscribe({
            sessionManager.saveAuthToken(it.token)
            loggedIn.value = Unit
        }, { exception ->
            error.value = (exception as HttpException).code()
        })
    }

}
