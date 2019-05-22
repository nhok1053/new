package jp.co.pise.studyapp.data.repository

import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import jp.co.pise.studyapp.data.entity.User

interface IRepository : Disposable

abstract class LoggedInParam constructor(@NonNull val user: User)