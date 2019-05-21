package jp.co.pise.study.domain.model

import jp.co.pise.study.data.entity.User

data class SaveUserChallenge(val entity: User)
data class SaveUserResult(val isSuccess: Boolean, val message: String? = null)