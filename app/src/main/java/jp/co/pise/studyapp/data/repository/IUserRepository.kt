package jp.co.pise.studyapp.data.repository

import io.reactivex.Single
import io.reactivex.annotations.NonNull
import jp.co.pise.studyapp.data.entity.User
import jp.co.pise.studyapp.domain.model.*

interface IUserRepository {
    fun login(model: LoginChallenge): Single<LoginResult>
    fun logout(model: LogoutChallenge): Single<LogoutResult>
    fun getUser(model: GetUserParam): Single<GetUserResult>
    fun saveUser(model: SaveUserChallenge): Single<SaveUserResult>
    fun getStoredUser(): Single<GetStoredUserResult>
    fun getStoredUser(model: GetStoredUserChallenge): Single<GetStoredUserResult>
}

class GetUserParam(@NonNull user: User) : LoggedInParam(user)