package jp.co.pise.studyapp.framework.rx

class RefreshedUsedCouponMessage : IMessage

class LoginExpiredMessage : IMessage

class UserNotLoggedInMessage : IMessage

class UserLoginStateChangeMessage(val isLogin: Boolean) : IMessage