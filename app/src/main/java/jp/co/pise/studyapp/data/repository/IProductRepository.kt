package jp.co.pise.studyapp.data.repository

import io.reactivex.Single
import jp.co.pise.studyapp.domain.model.GetProductChallenge
import jp.co.pise.studyapp.domain.model.GetProductResult
import jp.co.pise.studyapp.domain.model.SaveProductChallenge
import jp.co.pise.studyapp.domain.model.SaveProductResult

interface IProductRepository : IRepository {
    fun getProduct(model: GetProductChallenge): Single<GetProductResult>
    fun saveProduct(model: SaveProductChallenge): Single<SaveProductResult>
}