package jp.co.pise.studyapp.definition

import android.text.TextUtils

object Constant {
    const val DEFAULT_FORMAT_YMD = "yyyy-MM-dd"
    const val DEFAULT_FORMAT_YMDHM = "yyyy-MM-dd HH:mm"

    const val REQUEST_USE_COUPON = 0
}

object Message {
    const val DEFAULT_ERROR = "処理に失敗しました。\n再度お試し下さい。"
    const val LOGIN_EXPIRED_ERROR = "ログイン有効期限が切れました。\n再度ログインしてください。"
    const val LOGIN_FAILED = "ログイン処理に失敗しました。"
    const val AUTO_LOGIN_FAILED = "自動ログインに失敗しました。"
    const val LOGIN_PROMOTION = "アプリの機能を利用する為にログインしてください。"
    const val ID_PASSWORD_NOT_ENTERED = "ログインID、パスワードを入力してください。"

    const val LOGIN = "ログインに成功しました。"
    const val LOGOUT = "ログアウトしました。"

    const val REFRESH_USED_COUPON_SUCCESS = "クーポン利用情報をリセットしました。"

    const val USE_COUPON_ERROR = "クーポンの利用処理に失敗しました。"

    fun getResultMessage(resultCode: ResultCode, defaultMessage: String?): String {
        return if (TextUtils.isEmpty(defaultMessage)) {
            when (resultCode) {
                ResultCode.UserNotLogin,
                ResultCode.LoginExpired -> LOGIN_EXPIRED_ERROR

                else -> DEFAULT_ERROR
            }
        } else {
            defaultMessage!!
        }
    }
}

enum class ResultCode private constructor(val value: Int) {
    Success(0),
    LoginError(1),
    LoginExpired(3),
    UserNotLogin(4),
    OtherError(98),
    InternalError(99);


    companion object {

        fun findByValue(value: Int): ResultCode {
            var result = ResultCode.InternalError
            for (code in ResultCode.values()) {
                if (code.value == value) {
                    result = code
                    break
                }
            }
            return result
        }
    }
}