package jp.co.pise.studyapp.domain.usecase

import io.reactivex.Observable
import io.reactivex.Single
import jp.co.pise.studyapp.data.entity.News
import jp.co.pise.studyapp.data.repository.INewsRepository
import jp.co.pise.studyapp.domain.model.GetNewsItemModel
import jp.co.pise.studyapp.domain.model.GetNewsResult
import jp.co.pise.studyapp.domain.model.SaveNewsChallenge
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.onSafeError
import java.util.ArrayList
import javax.inject.Inject

class NewsDisplay @Inject constructor(private val newsRepository: INewsRepository):Usecase() {

    fun getNews(): Single<GetNewsResult> =
            this.newsRepository.getNews().flatMap<GetNewsResult> { getNewsResult ->
                Single.create { emitter ->
                    try {
                        val news: List<News> = if (getNewsResult.news != null) {
                            Observable.fromIterable<GetNewsItemModel>(getNewsResult.news)
                                    .map { it.toEntity() }
                                    .toList().blockingGet()
                        } else {
                            ArrayList()
                        }
                        val challenge = SaveNewsChallenge(news)
                        this.newsRepository.saveNews(challenge)
                                .subscribe({ emitter.onSuccess(getNewsResult) }, emitter::onSafeError).addBug(this.subscriptions)
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }
}