package jp.co.pise.studyapp.data.repository.impl

import com.squareup.moshi.Json
import io.reactivex.SingleEmitter
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import jp.co.pise.studyapp.data.repository.IRepository
import jp.co.pise.studyapp.definition.Message
import jp.co.pise.studyapp.definition.ResultCode
import jp.co.pise.studyapp.domain.model.ProductItemModel
import jp.co.pise.studyapp.extension.onSafeError
import jp.co.pise.studyapp.presentation.StudyAppException
import retrofit2.Response
import java.net.HttpURLConnection

abstract class BaseRepository : IRepository {
    protected var subscriptions = CompositeDisposable()

    fun <T : ApiResultModel> Response<T>.validate(): Boolean {
        val body = this.body()
        return (this.isSuccessful && this.code() == HttpURLConnection.HTTP_OK
                && body != null && body.apiResultCode == ApiResultCode.Success)
    }

    fun <T : ApiResultModel> SingleEmitter<*>.onSafeError(@NonNull response: Response<T>) {
        this.onSafeError(response.toException())
    }

    fun SingleEmitter<*>.onSafeError(apiResultCode: ApiResultCode) {
        this.onSafeError(apiResultCode.convert())
    }

    fun SingleEmitter<*>.onSafeError(resultCode: ResultCode) {
        this.onSafeError(StudyAppException.fromCode(resultCode))
    }

    private fun <T : ApiResultModel> Response<T>.toException(): StudyAppException {
        return this.toException(true)
    }

    private fun <T : ApiResultModel> Response<T>.toException(isUseResponseMessage: Boolean): StudyAppException {
        var resultCode = ResultCode.InternalError
        val errorMessage: String
        val body = this.body()

        when (this.code()) {
            HttpURLConnection.HTTP_OK -> resultCode = body?.apiResultCode?.convert() ?: ResultCode.InternalError
            HttpURLConnection.HTTP_UNAUTHORIZED -> resultCode = ResultCode.LoginExpired
        }

        errorMessage = if (body != null && isUseResponseMessage) {
            Message.getResultMessage(resultCode, body.errorMessage)
        } else {
            Message.getResultMessage(resultCode, null)
        }
        return StudyAppException.fromCode(resultCode, errorMessage)
    }

    override fun isDisposed(): Boolean {
        return this.subscriptions.isDisposed
    }

    override fun dispose() {
        if (!this.subscriptions.isDisposed) {
            this.subscriptions.dispose()
        }
    }
}

enum class ApiResultCode private constructor(val value: Int) {
    Success(0),
    LoginError(1),
    TokenNotSet(2),
    TokenExpired(3),
    UserNotLogin(4),
    OtherError(98),
    InternalError(99);

    fun convert(): ResultCode {
        var resultCode = ResultCode.InternalError
        when (findByValue(value)) {
            Success -> resultCode = ResultCode.Success
            LoginError -> resultCode = ResultCode.LoginError
            TokenNotSet, TokenExpired -> resultCode = ResultCode.LoginExpired
            UserNotLogin -> resultCode = ResultCode.UserNotLogin
            OtherError -> resultCode = ResultCode.OtherError
            else -> {
            }
        }
        return resultCode
    }

    companion object {
        fun findByValue(value: Int): ApiResultCode {
            var result = InternalError
            for (code in values()) {
                if (code.value == value) {
                    result = code
                    break
                }
            }
            return result
        }
    }
}

abstract class ApiResultModel {
    @Json(name = "resultCode")
    var resultCode: Int = ApiResultCode.InternalError.value
        protected set

    @Json(name = "errorMessage")
    var errorMessage: String? = null
        protected set

    val apiResultCode = ApiResultCode.findByValue(resultCode)
}

open class ProductItemApiModel {
    @Json(name = "id")
    lateinit var id: String
        protected set

    @Json(name = "name")
    var name: String? = null
        protected set

    @Json(name = "description")
    var description: String? = null
        protected set

    @Json(name = "imageUrl")
    var imageUrl: String? = null
        protected set

    @Json(name = "priceWithoutTax")
    var priceWithoutTax: Int = 0
        protected set

    @Json(name = "priceInTax")
    var priceInTax: Int = 0
        protected set

    fun convert(): ProductItemModel {
        return ProductItemModel(
                this.id,
                this.name,
                this.description,
                this.imageUrl,
                this.priceWithoutTax,
                this.priceInTax)
    }
}

open class CouponItemApiModel {
    @Json(name = "id")
    lateinit var id: String
        protected set

    @Json(name = "name")
    var name: String? = null
        protected set

    @Json(name = "imageUrl")
    var imageUrl: String? = null
        protected set

    @Json(name = "description")
    var description: String? = null
        protected set

    @Json(name = "priceWithoutTax")
    var priceWithoutTax: Int = 0
        protected set

    @Json(name = "priceInTax")
    var priceInTax: Int = 0
        protected set

    @Json(name = "productPriceWithoutTax")
    var productPriceWithoutTax: Int = 0
        protected set

    @Json(name = "productPriceInTax")
    var productPriceInTax: Int = 0
        protected set

    @Json(name = "startDate")
    var startDate: String? = null
        protected set

    @Json(name = "endDate")
    var endDate: String? = null
        protected set

    @Json(name = "usedLimit")
    var usedLimit: Int = 0
        protected set

    @Json(name = "sortOrder")
    var sortOrder: Int = 0
        protected set
}