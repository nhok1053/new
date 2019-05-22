package jp.co.pise.studyapp.domain.model

import io.reactivex.annotations.NonNull
import java.io.Serializable
import java.util.*

abstract class ProcessChallengeModel

abstract class LoggedInProcessChallengeModel() {
    constructor(@NonNull loginUser: LoginUser): this() {
        this.loginUser = loginUser
    }

    @NonNull
    lateinit var loginUser: LoginUser
        protected set
}

abstract class ProcessResultModel

open class ProductItemModel() : Serializable {

    constructor(id: String,
                name: String?,
                description: String?,
                imageUrl: String?,
                priceWithoutTax: Int,
                priceInTax: Int): this() {

        this.id = id
        this.name = name
        this.description = description
        this.imageUrl = imageUrl
        this.priceWithoutTax = priceWithoutTax
        this.priceInTax = priceInTax
    }

    lateinit var id: String
        protected set
    var name: String? = null
        protected set
    var description: String? = null
        protected set
    var imageUrl: String? = null
        protected set
    var priceWithoutTax: Int = 0
        protected set
    var priceInTax: Int = 0
        protected set
}

open class CouponItemModel() : Serializable {
    constructor(id: String,
                name: String?,
                imageUrl: String?,
                description: String?,
                priceWithoutTax: Int,
                priceInTax: Int,
                productPriceWithoutTax: Int,
                productPriceInTax: Int,
                startDate: Date?,
                endDate: Date?,
                usedLimit: Int,
                sortOrder: Int): this() {

        this.id = id
        this.name = name
        this.imageUrl = imageUrl
        this.description = description
        this.priceWithoutTax = priceWithoutTax
        this.priceInTax = priceInTax
        this.productPriceWithoutTax = productPriceWithoutTax
        this.productPriceInTax = productPriceInTax
        this.startDate = startDate
        this.endDate = endDate
        this.usedLimit = usedLimit
        this.sortOrder = sortOrder
    }

    lateinit var id: String
        protected set
    var name: String? = null
        protected set
    var imageUrl: String? = null
        protected set
    var description: String? = null
        protected set
    var priceWithoutTax: Int = 0
        protected set
    var priceInTax: Int = 0
        protected set
    var productPriceWithoutTax: Int = 0
        protected set
    var productPriceInTax: Int = 0
        protected set
    var startDate: Date? = null
        protected set
    var endDate: Date? = null
        protected set
    var usedLimit: Int = 0
        protected set
    var sortOrder: Int = 0
        protected set
}