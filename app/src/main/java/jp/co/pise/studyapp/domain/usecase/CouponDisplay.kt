package jp.co.pise.studyapp.domain.usecase

import io.reactivex.Single
import io.reactivex.annotations.NonNull
import jp.co.pise.studyapp.data.repository.GetCouponParam
import jp.co.pise.studyapp.data.repository.ICouponRepository
import jp.co.pise.studyapp.data.repository.IUserRepository
import jp.co.pise.studyapp.domain.model.GetCouponChallenge
import jp.co.pise.studyapp.domain.model.GetCouponResult
import jp.co.pise.studyapp.domain.model.GetStoredUserChallenge
import javax.inject.Inject

class CouponDisplay @Inject constructor(private val couponRepository: ICouponRepository, private val userRepository: IUserRepository) : Usecase() {

    fun getCoupon(@NonNull model: GetCouponChallenge): Single<GetCouponResult> =
            if (model.isLogin) { getCouponLogin(model) } else { getCouponNoLogin() }

    private fun getCouponLogin(@NonNull model: GetCouponChallenge): Single<GetCouponResult> =
            this.userRepository.getStoredUser(GetStoredUserChallenge(model.loginUser)).flatMap<GetCouponResult> {
                getStoredUserResult -> this.couponRepository.getCoupon(GetCouponParam(getStoredUserResult.toEntity()))
            }

    private fun getCouponNoLogin(): Single<GetCouponResult> = this.couponRepository.getCoupon()
}