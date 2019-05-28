package jp.co.pise.studyapp.framework.retrofit

import io.reactivex.Single
import jp.co.pise.studyapp.data.repository.impl.*
import jp.co.pise.studyapp.definition.ApiConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CouponApiInterface {

    @GET("api/coupon/getCoupon")
    fun getCoupon(): Single<Response<GetCouponApiResult>>

    @GET("api/coupon/getCoupon")
    fun getCoupon(@Header(ApiConfig.HEADER_AUTHORIZATION_KEY) authorization: String?): Single<Response<GetCouponApiResult>>

    @POST("api/coupon/useCoupon")
    fun useCoupon(@Header(ApiConfig.HEADER_AUTHORIZATION_KEY) authorization: String?, @Body model: UseCouponApiChallenge): Single<Response<UseCouponApiResult>>

    @GET("api/coupon/getUsedCoupon")
    fun getUsedCoupon(@Header(ApiConfig.HEADER_AUTHORIZATION_KEY) authorization: String?): Single<Response<GetUsedCouponApiResult>>

    @GET("api/coupon/refreshUsedCoupon")
    fun refreshUsedCoupon(@Header(ApiConfig.HEADER_AUTHORIZATION_KEY) authorization: String?): Single<Response<RefreshUsedCouponApiResult>>
}