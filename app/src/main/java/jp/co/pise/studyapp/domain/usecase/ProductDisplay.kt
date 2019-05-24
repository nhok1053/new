package jp.co.pise.studyapp.domain.usecase

import io.reactivex.Single
import jp.co.pise.studyapp.data.repository.IProductRepository
import jp.co.pise.studyapp.domain.model.GetProductChallenge
import jp.co.pise.studyapp.domain.model.GetProductResult
import javax.inject.Inject

class ProductDisplay @Inject constructor(private val productRepository: IProductRepository) : Usecase() {
    fun getProduct(): Single<GetProductResult> = this.productRepository.getProduct(GetProductChallenge())
}