package jp.co.pise.study.data.repository.impl

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import jp.co.pise.study.data.entity.OrmaDatabase
import jp.co.pise.study.data.entity.User
import jp.co.pise.study.data.repository.IUserRepository
import jp.co.pise.study.domain.model.LoginChallenge
import jp.co.pise.study.domain.model.LoginResult
import jp.co.pise.study.domain.model.SaveUserResult
import jp.co.pise.study.framework.retrofit.ApiInterface
import retrofit2.Retrofit
import java.net.HttpURLConnection
import javax.inject.Inject

class UserRepository @Inject constructor(retrofit: Retrofit, private val orma: OrmaDatabase) : IUserRepository {
    private val apiInterface = retrofit.create(ApiInterface::class.java)

    @SuppressLint("CheckResult")
    override fun login(model: LoginChallenge): LiveData<LoginResult> {
        val data = MutableLiveData<LoginResult>()
        try {
            apiInterface.login(model).subscribeOn(Schedulers.newThread()).subscribe(
                    {
                        val result = if (it.isSuccessful && it.code() == HttpURLConnection.HTTP_OK) {
                            it.body()
                        } else {
                            LoginResult(LoginResult.RESULT_NG, "error", null)
                        }
                        data.postValue(result)
                    },
                    { data.postValue(LoginResult(LoginResult.RESULT_NG, "error", null)) })
        } catch (e: Exception) {
            data.postValue(LoginResult(LoginResult.RESULT_NG, "error", null))
        }
        return data
    }

    override fun saveUser(user: User): LiveData<SaveUserResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}