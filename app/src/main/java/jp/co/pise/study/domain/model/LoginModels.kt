package jp.co.pise.study.domain.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class LoginChallenge(
        @Json(name = "user_id") val userId: String? = null,
        @Json(name = "password") val password: String? = null
)

@JsonSerializable
data class LoginResult(
        @Json(name = "result") val result: String = RESULT_NG,
        @Json(name = "message") val message: String? = null,
        @Json(name = "token") val token: String? = null
) {
    companion object {
        @JvmField val RESULT_OK = "OK"
        @JvmField val RESULT_NG = "NG"
    }

    fun isSuccess(): Boolean {
        return RESULT_OK == this.result
    }
}