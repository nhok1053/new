package jp.co.pise.studyapp.data.repository

import io.reactivex.Single
import io.reactivex.annotations.NonNull
import jp.co.pise.studyapp.data.entity.User
import jp.co.pise.studyapp.domain.model.GetCouponResult
import jp.co.pise.studyapp.domain.model.GetUsedCouponResult
import jp.co.pise.studyapp.domain.model.RefreshUsedCouponResult
import jp.co.pise.studyapp.domain.model.UseCouponResult

interface ICouponRepository : IRepository {
    fun getCoupon(): Single<GetCouponResult>
    fun getCoupon(model: GetCouponParam): Single<GetCouponResult>
    fun useCoupon(model: UseCouponParam): Single<UseCouponResult>
    fun getUsedCoupon(model: GetUsedCouponParam): Single<GetUsedCouponResult>
    fun refreshUsedCoupon(model: RefreshUsedCouponParam): Single<RefreshUsedCouponResult>
}

class GetCouponParam(@NonNull user: User) : LoggedInParam(user)

class UseCouponParam(@NonNull val couponId: String,
                     @NonNull user: User) : LoggedInParam(user)

class GetUsedCouponParam(@NonNull user: User) : LoggedInParam(user)

class RefreshUsedCouponParam(@NonNull user: User) : LoggedInParam(user)