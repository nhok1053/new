package jp.co.pise.study.framework.retrofit

import io.reactivex.Single
import jp.co.pise.study.domain.model.LoginChallenge
import jp.co.pise.study.domain.model.LoginResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body model: LoginChallenge): Single<Response<LoginResult>>
}