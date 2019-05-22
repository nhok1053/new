package jp.co.pise.studyapp.data.repository.impl

import android.text.TextUtils
import com.squareup.moshi.Json
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.co.pise.studyapp.data.entity.OrmaDatabase
import jp.co.pise.studyapp.data.repository.*
import jp.co.pise.studyapp.definition.Constant
import jp.co.pise.studyapp.definition.ResultCode
import jp.co.pise.studyapp.definition.Utility
import jp.co.pise.studyapp.domain.model.*
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.onSafeError
import jp.co.pise.studyapp.framework.retrofit.CouponApiInterface
import jp.co.pise.studyapp.framework.retrofit.UserApiInterface
import jp.co.pise.studyapp.presentation.StudyAppException
import java.util.*
import javax.inject.Inject

class CouponRepository @Inject constructor(private val couponApi: CouponApiInterface, userApi: UserApiInterface, db: OrmaDatabase) : JwtAuthRepository(userApi, db), ICouponRepository {

    override fun getCoupon(): Single<GetCouponResult> {
        return Single.create<GetCouponResult> { emitter ->
            try {
                this.couponApi.getCoupon().subscribeOn(Schedulers.io()).subscribe({ response ->
                    if (response.validate()) {
                        emitter.onSuccess(response.body()!!.convert())
                    } else {
                        emitter.onSafeError(response)
                    }
                }, emitter::onSafeError).addBug(this.subscriptions)
            } catch (e: Exception) {
                emitter.onSafeError(e)
            }
        }
    }

    override fun getCoupon(model: GetCouponParam): Single<GetCouponResult>
            = this.doGetCoupon.callWithTokenRefresh(GetCouponApiChallenge.fromModel(model)).map { it.convert() }

    override fun useCoupon(model: UseCouponParam): Single<UseCouponResult>
            = this.doUseCoupon.callWithTokenRefresh(UseCouponApiChallenge.fromModel(model)).map { it.convert() }

    override fun getUsedCoupon(model: GetUsedCouponParam): Single<GetUsedCouponResult>
            = this.doGetUsedCoupon.callWithTokenRefresh(GetUsedCouponApiChallenge.fromModel(model)).map { it.convert() }

    override fun refreshUsedCoupon(model: RefreshUsedCouponParam): Single<RefreshUsedCouponResult>
            = this.doRefreshUsedCoupon.callWithTokenRefresh(RefreshUsedCouponApiChallenge.fromModel(model)).map { it.convert() }

    // region <----- process ----->

    private val doGetCoupon: (model: GetCouponApiChallenge) -> Single<GetCouponApiResult> = {
        Single.create { emitter ->
            try {
                if (!TextUtils.isEmpty(it.accessToken)) {
                    this.couponApi.getCoupon(it.authorizationValue).subscribeOn(Schedulers.io()).subscribe({ response ->
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!)
                        } else {
                            emitter.onSafeError(response)
                        }
                    }, emitter::onSafeError).addBug(this.subscriptions)
                } else {
                    emitter.onSafeError(StudyAppException.fromCode(ResultCode.LoginExpired))
                }
            } catch (e: Exception) {
                emitter.onSafeError(e)
            }
        }
    }

    private val doUseCoupon: (model: UseCouponApiChallenge) -> Single<UseCouponApiResult> = {
        Single.create { emitter ->
            try {
                if (!TextUtils.isEmpty(it.accessToken)) {
                    this.couponApi.useCoupon(it.authorizationValue, it).subscribeOn(Schedulers.io()).subscribe({ response ->
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!)
                        } else {
                            emitter.onSafeError(response)
                        }
                    }, emitter::onSafeError).addBug(this.subscriptions)
                } else {
                    emitter.onSafeError(StudyAppException.fromCode(ResultCode.LoginExpired))
                }
            } catch (e: Exception) {
                emitter.onSafeError(e)
            }
        }
    }

    private val doGetUsedCoupon: (model: GetUsedCouponApiChallenge) -> Single<GetUsedCouponApiResult> = {
        Single.create { emitter ->
            try {
                if (!TextUtils.isEmpty(it.accessToken)) {
                    this.couponApi.getUsedCoupon(it.authorizationValue).subscribeOn(Schedulers.io()).subscribe({ response ->
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!)
                        } else {
                            emitter.onSafeError(response)
                        }
                    }, emitter::onSafeError).addBug(this.subscriptions)
                } else {
                    emitter.onSafeError(StudyAppException.fromCode(ResultCode.LoginExpired))
                }
            } catch (e: Exception) {
                emitter.onSafeError(e)
            }
        }
    }

    private val doRefreshUsedCoupon: (RefreshUsedCouponApiChallenge) -> Single<RefreshUsedCouponApiResult> = {
        Single.create { emitter ->
            try {
                if (!TextUtils.isEmpty(it.accessToken)) {
                    this.couponApi.refreshUsedCoupon(it.authorizationValue).subscribeOn(Schedulers.io()).subscribe({ response ->
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!)
                        } else {
                            emitter.onSafeError(response)
                        }
                    }, emitter::onSafeError).addBug(this.subscriptions)
                } else {
                    emitter.onSafeError(StudyAppException.fromCode(ResultCode.LoginExpired))
                }
            } catch (e: Exception) {
                emitter.onSafeError(e)
            }
        }
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

        fun convert(): GetCouponResult {
            var items: List<GetCouponItemModel> = ArrayList()
            if (this.coupons != null) {
                items = Observable.fromIterable(this.coupons)
                        .map { it.convert() }
                        .toList().blockingGet()
            }
            return GetCouponResult(items)
        }
    }

    class GetCouponItemApiModel : CouponItemApiModel() {
        @Json(name = "usedCount")
        var usedCount: Int? = null

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
        lateinit var couponId: String

        @Json(name = "usedCount")
        var usedCount: Int = 0

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

        fun convert(): GetUsedCouponResult {
            var items: List<GetUsedCouponItemModel> = ArrayList()
            if (this.usedCoupons != null) {
                items = Observable.fromIterable(this.usedCoupons)
                        .map { it.convert() }
                        .toList().blockingGet()
            }
            return GetUsedCouponResult(items)
        }
    }

    class GetUsedCouponItemApiModel : CouponItemApiModel() {
        @Json(name = "usedTime")
        lateinit var usedTime: String

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