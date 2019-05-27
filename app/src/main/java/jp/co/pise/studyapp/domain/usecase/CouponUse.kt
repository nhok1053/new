package jp.co.pise.studyapp.domain.usecase

import io.reactivex.Single
import io.reactivex.annotations.NonNull
import jp.co.pise.studyapp.data.repository.*
import jp.co.pise.studyapp.domain.model.*
import jp.co.pise.studyapp.extension.addBug
import javax.inject.Inject

class CouponUse @Inject constructor(private val couponRepository: ICouponRepository, private val userRepository: IUserRepository) : Usecase() {
    init {
        this.couponRepository.addBug(this.subscriptions)
        this.userRepository.addBug(this.subscriptions)
    }

    fun useCoupon(@NonNull model: UseCouponChallenge): Single<UseCouponResult> =
            this.userRepository.getStoredUser(GetStoredUserChallenge(model.loginUser)).flatMap<UseCouponResult> {
                getStoredUserResult -> this.couponRepository.useCoupon(UseCouponParam(model.couponId, getStoredUserResult.toEntity()))
            }

    fun getUsedCoupon(@NonNull model: GetUsedCouponChallenge): Single<GetUsedCouponResult> =
            this.userRepository.getStoredUser(GetStoredUserChallenge(model.loginUser)).flatMap<GetUsedCouponResult> {
                getStoredUserResult -> this.couponRepository.getUsedCoupon(GetUsedCouponParam(getStoredUserResult.toEntity()))
            }

    fun refreshUsedCoupon(model: RefreshUsedCouponChallenge): Single<RefreshUsedCouponResult> =
            this.userRepository.getStoredUser(GetStoredUserChallenge(model.loginUser)).flatMap<RefreshUsedCouponResult> {
                getStoredUserResult -> this.couponRepository.refreshUsedCoupon(RefreshUsedCouponParam(getStoredUserResult.toEntity()))
            }
}