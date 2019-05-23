package jp.co.pise.studyapp.data.repository.impl

import com.squareup.moshi.Json
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.co.pise.studyapp.data.entity.OrmaDatabase
import jp.co.pise.studyapp.data.repository.IProductRepository
import jp.co.pise.studyapp.domain.model.*
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.onSafeError
import jp.co.pise.studyapp.framework.retrofit.ProductApiInterface
import java.util.ArrayList
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productApi: ProductApiInterface, private val db: OrmaDatabase) : BaseRepository(), IProductRepository {

    override fun getProduct(model: GetProductChallenge): Single<GetProductResult> =
            Single.create<GetProductResult> { emitter ->
                try {
                    this.productApi.getProduct().subscribeOn(Schedulers.io()).subscribe({ response ->
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    }, emitter::onSafeError).addBug(this.subscriptions)
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }

    override fun saveProduct(model: SaveProductChallenge): Single<SaveProductResult> =
            Single.create<SaveProductResult> { emitter ->
                try {
                    this.db.transactionSync {
                        this.db.deleteFromProduct().execute()
                        model.products?.apply {
                            this.forEach { db.insertIntoProduct(it) }
                        }
                        emitter.onSuccess(SaveProductResult())
                    }
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }.subscribeOn(Schedulers.io())
}

// region <----- models ----->

class GetProductApiResult : ApiResultModel() {
    @Json(name = "products")
    var products: List<ProductItemApiModel>? = null
        private set

    fun convert(): GetProductResult {
        var items: List<ProductItemModel> = ArrayList()
        this.products?.apply {
            items = Observable.fromIterable(this)
                    .map { it.convert() }
                    .toList().blockingGet()
        }
        return GetProductResult(items)
    }
}

// endregion