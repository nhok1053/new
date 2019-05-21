package jp.co.pise.study.data.repository

import android.arch.lifecycle.LiveData
import jp.co.pise.study.data.entity.User
import jp.co.pise.study.domain.model.LoginChallenge
import jp.co.pise.study.domain.model.LoginResult
import jp.co.pise.study.domain.model.SaveUserResult

interface IUserRepository : IRepository {
    fun login(model: LoginChallenge): LiveData<LoginResult>
    fun saveUser(user: User): LiveData<SaveUserResult>
}