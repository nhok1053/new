package jp.co.pise.studyapp.data.repository.impl

import com.squareup.moshi.Json
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.co.pise.studyapp.data.entity.OrmaDatabase
import jp.co.pise.studyapp.data.repository.INewsRepository
import jp.co.pise.studyapp.domain.model.GetNewsItemModel
import jp.co.pise.studyapp.domain.model.GetNewsResult
import jp.co.pise.studyapp.domain.model.SaveNewsChallenge
import jp.co.pise.studyapp.domain.model.SaveNewsResult
import jp.co.pise.studyapp.extension.onSafeError
import jp.co.pise.studyapp.framework.retrofit.NewsApiInterface
import java.util.ArrayList
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApiInterface, private val db: OrmaDatabase) : BaseRepository(), INewsRepository {

    override fun getNews(): Single<GetNewsResult> =
            Single.create<GetNewsResult> { emitter ->
                try {
                    val disposable = this.newsApi.getNews().subscribeOn(Schedulers.io()).subscribe({ response ->
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    }, emitter::onSafeError)
                    this.subscriptions.add(disposable)
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }

    override fun saveNews(model: SaveNewsChallenge): Single<SaveNewsResult> =
            Single.create<SaveNewsResult> { emitter ->
                try {
                    this.db.transactionSync {
                        this.db.deleteFromNews().execute()
                        model.news?.forEach { db.insertIntoNews(it) }
                    }
                    emitter.onSuccess(SaveNewsResult())
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }.subscribeOn(Schedulers.io())
}

// region <----- models ----->

class GetNewsApiResult : ApiResultModel() {
    @Json(name = "news")
    var news: List<GetNewsItemApiModel>? = null
        private set

    fun convert(): GetNewsResult {
        var items: List<GetNewsItemModel> = if (this.news != null) {
            this.news!!.map { it.convert() }
        } else {
            ArrayList()
        }
        return GetNewsResult(items)
    }
}

class GetNewsItemApiModel {
    @Json(name = "id")
    lateinit var id: String
        private set

    @Json(name = "imageUrl")
    var imageUrl: String? = null
        private set

    @Json(name = "description")
    var description: String? = null
        private set

    @Json(name = "url")
    var url: String? = null
        private set

    @Json(name = "sortOrder")
    var sortOrder: Int = 0
        private set

    fun convert(): GetNewsItemModel {
        return GetNewsItemModel(
                this.id,
                this.imageUrl,
                this.description,
                this.url,
                this.sortOrder)
    }
}

// endregion
