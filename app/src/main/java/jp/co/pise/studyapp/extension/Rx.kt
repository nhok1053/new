package jp.co.pise.studyapp.extension

import io.reactivex.SingleEmitter
import jp.co.pise.studyapp.presentation.StudyAppException

fun SingleEmitter<*>.onSafeError(t: Throwable) {
    if (!this.isDisposed) this.onError(StudyAppException.fromThrowable(t))
}