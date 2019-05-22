package jp.co.pise.studyapp.presentation

import jp.co.pise.studyapp.definition.Message
import jp.co.pise.studyapp.definition.ResultCode
import java.lang.Exception

class StudyAppException private constructor(t: Throwable?, val resultCode: ResultCode, displayMessage: String? = null) : Exception(t) {
    val displayMessage: String = Message.getResultMessage(resultCode, displayMessage)

    private constructor(resultCode: ResultCode, displayMessage: String?) : this(null, resultCode, displayMessage)

    companion object {
        fun fromThrowable(t: Throwable): StudyAppException {
            val result: StudyAppException
            if (t is StudyAppException) {
                result = t
            } else {
                result = StudyAppException(t, ResultCode.InternalError, null)
            }
            return result
        }

        @JvmOverloads
        fun fromCode(resultCode: ResultCode, displayMessage: String? = null): StudyAppException {
            return StudyAppException(resultCode, displayMessage)
        }
    }
}