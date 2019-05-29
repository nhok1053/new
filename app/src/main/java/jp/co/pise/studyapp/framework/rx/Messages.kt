package jp.co.pise.studyapp.framework.rx

import jp.co.pise.studyapp.domain.model.LoginUser

class RefreshedUsedCouponMessage : IMessage

class LoginExpiredMessage : IMessage

class UserNotLoggedInMessage : IMessage

class LoginStateChangeMessage(val isLogin: Boolean, val loginUser: LoginUser?) : IMessage