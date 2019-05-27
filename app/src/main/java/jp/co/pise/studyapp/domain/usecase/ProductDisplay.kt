package jp.co.pise.studyapp.domain.usecase

import io.reactivex.Single
import jp.co.pise.studyapp.data.repository.IProductRepository
import jp.co.pise.studyapp.domain.model.GetProductChallenge
import jp.co.pise.studyapp.domain.model.GetProductResult
import jp.co.pise.studyapp.extension.addBug
import javax.inject.Inject

class ProductDisplay @Inject constructor(private val productRepository: IProductRepository) : Usecase() {
    init {
        this.productRepository.addBug(this.subscriptions)
    }

    fun getProduct(): Single<GetProductResult> = this.productRepository.getProduct(GetProductChallenge())
}