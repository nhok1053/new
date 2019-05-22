package jp.co.pise.studyapp.data.repository

import io.reactivex.Single
import jp.co.pise.studyapp.domain.model.GetNewsResult
import jp.co.pise.studyapp.domain.model.SaveNewsChallenge
import jp.co.pise.studyapp.domain.model.SaveNewsResult

interface INewsRepository {
    fun getNews(): Single<GetNewsResult>
    fun saveNews(model: SaveNewsChallenge): Single<SaveNewsResult>
}