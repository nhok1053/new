package jp.co.pise.studyapp.domain.model

import jp.co.pise.studyapp.data.entity.Product
import java.io.Serializable

class GetProductChallenge : ProcessChallengeModel()

class GetProductResult(val products: List<ProductItemModel>? = null) : ProcessResultModel()

class SaveProductChallenge(val products: List<Product>? = null) : ProcessChallengeModel()

class SaveProductResult : ProcessResultModel()