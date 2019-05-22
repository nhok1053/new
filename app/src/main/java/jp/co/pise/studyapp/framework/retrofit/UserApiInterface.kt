package jp.co.pise.studyapp.framework.retrofit

import io.reactivex.Single
import jp.co.pise.studyapp.data.repository.impl.RefreshTokenApiChallenge
import jp.co.pise.studyapp.data.repository.impl.RefreshTokenApiResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiInterface {

//    @POST("api/user/login")
//    fun login(@Body model: LoginApiChallenge): Single<Response<LoginApiResult>>

//    @POST("api/user/getUser")
//    fun getUser(@Header(ApiConfig.HEADER_AUTHORIZATION_KEY) authorization: String, @Body model: GetUserApiChallenge): Single<Response<GetUserApiResult>>

    @POST("api/user/refreshToken")
    fun refreshToken(@Body model: RefreshTokenApiChallenge): Single<Response<RefreshTokenApiResult>>
}