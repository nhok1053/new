package jp.co.pise.studyapp.extension

val Boolean?.unwrap get() = this ?: false
val Int?.unwrap get() = this ?: 0