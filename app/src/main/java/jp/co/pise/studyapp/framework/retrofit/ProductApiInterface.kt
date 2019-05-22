package jp.co.pise.studyapp.framework.retrofit

import io.reactivex.Single
import jp.co.pise.studyapp.data.repository.impl.GetProductApiResult
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiInterface {

    @GET("api/product/getProduct")
    fun getProduct(): Single<Response<GetProductApiResult>>
}