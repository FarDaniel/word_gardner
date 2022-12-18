package aut.dipterv.word_gardner.network_data.apis

import aut.dipterv.word_gardner.network_data.models.LoginModel
import aut.dipterv.word_gardner.network_data.models.TokenModel
import aut.dipterv.word_gardner.network_data.models.UserModel
import aut.dipterv.word_gardner.network_data.models.dtos.CountDto
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import io.reactivex.Single
import retrofit2.http.*

interface UserApi {

    companion object {
        private const val BASE_PATH = "data"
        const val USERS = "$BASE_PATH/user"
        const val USER = "$USERS/{id}"
        const val REGISTER = "user"
        const val LOGIN = "user/login"
        const val FILTERED_USERS = "$USERS/filtered/{limit}/{offset}"
        const val COUNT = "$USERS/count"
    }

    @GET(USERS)
    fun getAllUsers(): Single<List<UserModel>>

    @POST(USERS)
    fun addUser(user: LoginModel): Single<String>

    @PUT(USERS)
    fun updateUser(
        @Body() user: UserModel
    )

    @GET(USER)
    fun getUser(
        @Path("id") userId: Long
    ): Single<UserModel>


    @DELETE(USER)
    fun deleteUser(
        @Path("id") userId: String
    )

    @PATCH(FILTERED_USERS)
    fun getUsersByFilter(
        @Body filter: SearchFilter,
        @Path("limit") limit: Int,
        @Path("offset") offset: Int
    ): Single<List<UserModel>>

    @PATCH(COUNT)
    fun getUserCnt(
        @Body filter: SearchFilter
    ): Single<CountDto>

    @POST(REGISTER)
    fun register(
        @Body user: LoginModel
    ): Single<TokenModel>

    @POST(LOGIN)
    fun login(
        @Body user: LoginModel
    ): Single<TokenModel>

}
