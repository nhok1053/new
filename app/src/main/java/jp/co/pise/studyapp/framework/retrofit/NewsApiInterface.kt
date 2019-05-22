package jp.co.pise.studyapp.framework.retrofit

import io.reactivex.Single
import jp.co.pise.studyapp.data.repository.impl.GetNewsApiResult
import retrofit2.Response
import retrofit2.http.GET

interface NewsApiInterface {

    @GET("api/news/getNews")
    fun getNews(): Single<Response<GetNewsApiResult>>
}