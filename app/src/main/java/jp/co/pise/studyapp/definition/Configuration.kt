package jp.co.pise.studyapp.definition

import android.text.TextUtils

object ApiConfig {
    const val REQUEST_METHOD_GET = "GET"
    const val REQUEST_METHOD_POST = "POST"

    const val CONNECTION_TIMEOUT_SEC: Long = 30
    const val READ_TIMEOUT_SEC: Long = 30

    const val CONTENT_TYPE = "Content-Type"
    const val CONTENT_TYPE_JSON_UTF8 = "application/json; charset=UTF-8"

    const val X_API_KEY = "X-Api-Key"
    const val X_API_KEY_VALUE = "StudyAppApi"

    const val X_API_CLIENT = "X-Api-Client"
    const val X_API_CLIENT_VALUE = "StudyAppClient"

    const val X_API_DEVICE = "X-Api-Device"
    const val X_API_DEVICE_VALUE = "Android"

    const val HEADER_AUTHORIZATION_KEY = "Authorization"
    const val AUTHORIZATION_SCHEMA = "Bearer"

    fun authorizationValue(accessToken: String) =
            if (!TextUtils.isEmpty(accessToken)) "$AUTHORIZATION_SCHEMA $accessToken"
            else null
}

object DbConfig {
    const val DB_NAME = "StudyDb"
    const val DB_VERSION_1 = 1
    const val DB_VERSION_NEWEST = DB_VERSION_1
}