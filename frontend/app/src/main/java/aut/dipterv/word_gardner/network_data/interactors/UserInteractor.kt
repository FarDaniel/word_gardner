package aut.dipterv.word_gardner.network_data.interactors

import aut.dipterv.word_gardner.network_data.apis.UserApi
import aut.dipterv.word_gardner.network_data.models.LoginModel
import aut.dipterv.word_gardner.network_data.models.TokenModel
import aut.dipterv.word_gardner.network_data.models.UserModel
import aut.dipterv.word_gardner.network_data.models.dtos.CountDto
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserInteractor(private val userApi: UserApi) {

    fun getAllUsers(): Single<List<UserModel>> {
        return userApi.getAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addUser(user: LoginModel): Single<String> {
        return userApi.addUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateUser(user: UserModel) {
        userApi.updateUser(user)
    }

    fun getUser(userId: Long): Single<UserModel> {
        return userApi.getUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteUser(userId: String) {
        userApi.deleteUser(userId)
    }

    fun getUsersByFilter(filter: SearchFilter, limit: Int, offset: Int): Single<List<UserModel>> {
        return userApi.getUsersByFilter(filter, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserCnt(filter: SearchFilter): Single<CountDto> {
        return userApi.getUserCnt(filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun login(loginModel: LoginModel): Single<TokenModel> {
        return userApi.login(loginModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun register(loginModel: LoginModel): Single<TokenModel> {
        return userApi.register(loginModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
