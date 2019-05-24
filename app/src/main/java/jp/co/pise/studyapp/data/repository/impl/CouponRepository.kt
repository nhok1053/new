package jp.co.pise.studyapp.data.repository.impl

import com.squareup.moshi.Json
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.co.pise.studyapp.data.entity.OrmaDatabase
import jp.co.pise.studyapp.data.repository.*
import jp.co.pise.studyapp.definition.Constant
import jp.co.pise.studyapp.definition.Utility
import jp.co.pise.studyapp.domain.model.*
import jp.co.pise.studyapp.extension.onSafeError
import jp.co.pise.studyapp.framework.retrofit.CouponApiInterface
import jp.co.pise.studyapp.framework.retrofit.UserApiInterface
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class CouponRepository @Inject constructor(private val couponApi: CouponApiInterface, userApi: UserApiInterface, db: OrmaDatabase) : JwtAuthRepository(userApi, db), ICouponRepository {

    override fun getCoupon(): Single<GetCouponResult> =
            this.couponApi.getCoupon().subscribeOn(Schedulers.io()).flatMap { response ->
                Single.create<GetCouponResult> { emitter ->
                    try {
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }

    override fun getCoupon(model: GetCouponParam): Single<GetCouponResult> =
            this.getCouponApi.callWithTokenRefresh(GetCouponApiChallenge.fromModel(model)).flatMap { response ->
                Single.create<GetCouponResult> { emitter ->
                    try {
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }


    override fun useCoupon(model: UseCouponParam): Single<UseCouponResult> =
            this.useCouponApi.callWithTokenRefresh(UseCouponApiChallenge.fromModel(model)).flatMap { response ->
                Single.create<UseCouponResult> { emitter ->
                    try {
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }

    override fun getUsedCoupon(model: GetUsedCouponParam): Single<GetUsedCouponResult> =
            this.getUsedCouponApi.callWithTokenRefresh(GetUsedCouponApiChallenge.fromModel(model)).flatMap { response ->
                Single.create<GetUsedCouponResult> { emitter ->
                    try {
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }

    override fun refreshUsedCoupon(model: RefreshUsedCouponParam): Single<RefreshUsedCouponResult> =
            this.refreshUsedCouponApi.callWithTokenRefresh(RefreshUsedCouponApiChallenge.fromModel(model)).flatMap { response ->
                Single.create<RefreshUsedCouponResult> { emitter ->
                    try {
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }

    // region <----- process ----->

    private val getCouponApi: (model: GetCouponApiChallenge) -> Single<Response<GetCouponApiResult>> = {
        this.couponApi.getCoupon(it.authorizationValue).subscribeOn(Schedulers.io())
    }

    private val useCouponApi: (model: UseCouponApiChallenge) -> Single<Response<UseCouponApiResult>> = {
        this.couponApi.useCoupon(it.authorizationValue, it).subscribeOn(Schedulers.io())
    }

    private val getUsedCouponApi: (model: GetUsedCouponApiChallenge) -> Single<Response<GetUsedCouponApiResult>> = {
        this.couponApi.getUsedCoupon(it.authorizationValue).subscribeOn(Schedulers.io())
    }

    private val refreshUsedCouponApi: (RefreshUsedCouponApiChallenge) -> Single<Response<RefreshUsedCouponApiResult>> = {
        this.couponApi.refreshUsedCoupon(it.authorizationValue).subscribeOn(Schedulers.io())
    }

    // endregion

    //region <------ models ----->

    class GetCouponApiChallenge(id: String,
                                accessToken: String) : JwtAuthChallengeModel(id, accessToken) {

        companion object {
            fun fromModel(param: GetCouponParam)
                    = GetCouponApiChallenge(param.user.id, param.user.accessToken)
        }
    }

    class GetCouponApiResult : ApiResultModel() {
        @Json(name = "coupons")
        var coupons: List<GetCouponItemApiModel>? = null
            private set

        fun convert(): GetCouponResult {
            val items: List<GetCouponItemModel> = if (this.coupons != null) {
                this.coupons!!.map { it.convert() }
            } else {
                ArrayList()
            }
            return GetCouponResult(items)
        }
    }

    class GetCouponItemApiModel : CouponItemApiModel() {
        @Json(name = "usedCount")
        var usedCount: Int? = null
            private set

        fun convert(): GetCouponItemModel {
            return GetCouponItemModel(
                    this.id,
                    this.name,
                    this.imageUrl,
                    this.description,
                    this.priceWithoutTax,
                    this.priceInTax,
                    this.productPriceWithoutTax,
                    this.productPriceInTax,
                    Utility.stringParseDate(this.startDate, Constant.DEFAULT_FORMAT_YMDHM),
                    Utility.stringParseDate(this.endDate, Constant.DEFAULT_FORMAT_YMDHM),
                    this.usedLimit,
                    this.sortOrder,
                    this.usedCount)
        }
    }

    class UseCouponApiChallenge(@Json(name = "couponId") val couponId: String,
                                id: String,
                                accessToken: String) : JwtAuthChallengeModel(id, accessToken) {
        companion object {
            fun fromModel(model: UseCouponParam)
                    = UseCouponApiChallenge(model.couponId, model.user.id, model.user.accessToken)
        }
    }

    class UseCouponApiResult : ApiResultModel() {
        @Json(name = "couponId")
        var couponId: String? = null
            private set

        @Json(name = "usedCount")
        var usedCount: Int = 0
            private set

        fun convert(): UseCouponResult {
            return UseCouponResult(
                    this.couponId,
                    this.usedCount)
        }
    }

    class GetUsedCouponApiChallenge(id: String,
                                    accessToken: String) : JwtAuthChallengeModel(id, accessToken) {
        companion object {
            fun fromModel(model: GetUsedCouponParam)
                    = GetUsedCouponApiChallenge(model.user.id, model.user.accessToken)
        }
    }

    class GetUsedCouponApiResult : ApiResultModel() {
        @Json(name = "usedCoupons")
        var usedCoupons: List<GetUsedCouponItemApiModel>? = null
            private set

        fun convert(): GetUsedCouponResult {
            var items: List<GetUsedCouponItemModel> = if (this.usedCoupons != null) {
                this.usedCoupons!!.map { it.convert() }
            } else {
                ArrayList()
            }
            return GetUsedCouponResult(items)
        }
    }

    class GetUsedCouponItemApiModel : CouponItemApiModel() {
        @Json(name = "usedTime")
        lateinit var usedTime: String
            private set

        fun convert(): GetUsedCouponItemModel {
            return GetUsedCouponItemModel(
                    this.id,
                    this.name,
                    this.imageUrl,
                    this.description,
                    this.priceWithoutTax,
                    this.priceInTax,
                    this.productPriceWithoutTax,
                    this.productPriceInTax,
                    Utility.stringParseDate(this.startDate, Constant.DEFAULT_FORMAT_YMDHM),
                    Utility.stringParseDate(this.endDate, Constant.DEFAULT_FORMAT_YMDHM),
                    this.usedLimit,
                    this.sortOrder,
                    Utility.stringParseDate(this.usedTime, Constant.DEFAULT_FORMAT_YMDHM))
        }
    }

    class RefreshUsedCouponApiChallenge(id: String,
                                        accessToken: String) : JwtAuthChallengeModel(id, accessToken) {
        companion object {
            fun fromModel(model: RefreshUsedCouponParam)
                    = RefreshUsedCouponApiChallenge(model.user.id, model.user.accessToken)
        }
    }

    class RefreshUsedCouponApiResult : ApiResultModel() {
        fun convert(): RefreshUsedCouponResult {
            return RefreshUsedCouponResult()
        }
    }

    //endregion
}