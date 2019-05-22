package jp.co.pise.studyapp.domain.model

import io.reactivex.annotations.NonNull
import java.util.*

class GetCouponChallenge(val isLogin: Boolean,
                         @NonNull loginUser: LoginUser) : LoggedInProcessChallengeModel(loginUser)

class GetCouponResult(val coupons: List<GetCouponItemModel>?) : ProcessResultModel()

class GetCouponItemModel(id: String,
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
                         sortOrder: Int,
                         usedCount: Int?): CouponItemModel() {

    var usedCount: Int? = null
        private set

    init {
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
        this.usedCount = usedCount
    }
}

class UseCouponChallenge(val couponId: String,
                         @NonNull loginUser: LoginUser) : LoggedInProcessChallengeModel(loginUser)

class UseCouponResult(val couponId: String?,
                      val usedCount: Int) : ProcessResultModel()

class GetUsedCouponChallenge(@NonNull loginUser: LoginUser) : LoggedInProcessChallengeModel(loginUser)

class GetUsedCouponResult(val usedCoupons: List<GetUsedCouponItemModel>? = null) : ProcessResultModel()

class GetUsedCouponItemModel(id: String,
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
                             sortOrder: Int,
                             usedTime: Date?): CouponItemModel() {

    var usedTime: Date? = null
        private set

    init {
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
        this.usedTime = usedTime
    }
}

class RefreshUsedCouponChallenge(@NonNull loginUser: LoginUser) : LoggedInProcessChallengeModel(loginUser)

class RefreshUsedCouponResult : ProcessResultModel()